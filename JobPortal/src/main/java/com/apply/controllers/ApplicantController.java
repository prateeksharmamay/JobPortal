package com.apply.controllers;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.opsworks.model.App;
import com.apply.entities.*;
import com.apply.services.ApplicantService;
import com.apply.services.ApplicationService;
import com.apply.services.OpeningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Vivek Agarwal on 5/9/2017.
 */

@RestController
@RequestMapping(value = "/applicant")
public class ApplicantController {
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private OpeningService openingService;

    @GetMapping(value = "/profile")
    @ResponseBody
    public ModelMap getProfile(ModelMap model,HttpSession session){
        //System.out.println("Trying to get the applicant with email :"+email);
        ModelMap map = new ModelMap();
        if(session.getAttribute("email")==null) {
            System.out.println("Not signed in");
            map.addAttribute("statusCode", "400");
            return map;
        }

        try {
            Applicant applicant=applicantService.fetch(session.getAttribute("email").toString());
            System.out.println("Found applicant "+applicant.toString());
            map.addAttribute("profile", applicant);
            map.addAttribute("statusCode", "200");
            return map;
        }
        catch(Exception e){
            e.printStackTrace();
            map.addAttribute("statusCode", "400");
            return map;
        }
    }

    @GetMapping(value = "/activeSession")
    @ResponseBody
    public ModelMap activeSession(HttpSession session){
        ModelMap responseMap = new ModelMap();
        if(session.getAttribute("email")!=null){
            try {
                System.out.println("Email for session in company :"+session.getAttribute("email").toString());
                Applicant applicant=applicantService.fetch(session.getAttribute("email").toString());
                if(applicant==null){
                    responseMap.addAttribute("statusCode","404");
                    return responseMap;
                }
                responseMap.addAttribute("statusCode","200");

            } catch (Exception e) {
                e.printStackTrace();
                responseMap.addAttribute("statusCode","404");
            }
            return responseMap;
        }
        else{
            responseMap.addAttribute("statusCode","404");
            return responseMap;
        }
    }
//original
//    @GetMapping(value = "/allJobs")
//    public Set<Opening> getOpenings(HttpSession session, Pageable pageable){
//        if(session.getAttribute("email")==null) {
//            System.out.println(" Please login first");
//            return null;
//        }
//        try {
//            Page<Opening> openings = applicantService.getOpenings(pageable);
//            Set<Opening> open=new HashSet<>();
//            for(Opening opening:openings)
//                open.add(opening);
//            return open;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }


    @GetMapping(value = "/allJobs")
    public  ModelMap  getOpenings(HttpSession session, Pageable pageable){
        ModelMap responseMap = new ModelMap();
        try {
        if(session.getAttribute("email")==null) {
            System.out.println(" Please login first");
            return null;
        }
            Page<Opening> openings = applicantService.getOpenings(pageable);
            if(openings!=null){
                List<Opening> openingsList=new ArrayList<>();
                List<String> imageURLs=new ArrayList<>();
                for(Opening o : openings){
                    openingsList.add(o);
                    imageURLs.add(o.getCompany().getImageUrl());
                }

                responseMap.addAttribute("openings",openingsList);
                responseMap.addAttribute("imageURLs",imageURLs);
                responseMap.addAttribute("message","Search Results-");
                responseMap.addAttribute("status","200");
                return responseMap;
            }else{
                responseMap.addAttribute("message","No Jobs Currently Available");
                responseMap.addAttribute("status","404");
                return responseMap;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("message","Snap! Something went wrong. Please try again.");
            responseMap.addAttribute("status","404");
            return responseMap;
        }
    }





    @GetMapping(value = "/search")
    @ResponseBody
    public ModelMap search(@RequestParam(value= "query") String query, HttpSession session, Pageable pageable) {
        ModelMap responseMap = new ModelMap();
        try {
            if (session.getAttribute("email") == null) {
                responseMap.addAttribute("statusCode","404");
                responseMap.addAttribute("message","Please login to search");
                return responseMap;
            }
            Page<Opening> openings=openingService.searchOpenings(query,pageable);

            if(openings!=null){

                List<Opening> openingsList=new ArrayList<>();
                List<String> imageURLs=new ArrayList<>();
                for(Opening o : openings){
//                    imageMap.put(o,o.getCompany().getImageUrl());
                    openingsList.add(o);
                    imageURLs.add(o.getCompany().getImageUrl());
                }

                responseMap.addAttribute("openings",openingsList);
                responseMap.addAttribute("imageURLs",imageURLs);
                responseMap.addAttribute("message","Search Results-");
                responseMap.addAttribute("statusCode","200");
                return responseMap;
            }
            else{
                responseMap.addAttribute("message","Your search returned no results");
                responseMap.addAttribute("status","404");
                return responseMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.addAttribute("message","Snap! Something went wrong. Please try again.");
            responseMap.addAttribute("message","Snap! Something went wrong. Please try again.");
            responseMap.addAttribute("status","404");
            return responseMap;
        }
    }

    @GetMapping(value = "/searchWithFilter")
    @ResponseBody
    public ModelMap searchWithFilter(@RequestParam(value = "query") String query,
                                     @RequestParam(value = "keywords") Set<String> keywords,
                                     @RequestParam(value="companies")Set<String> companyNames,
                                     @RequestParam(value = "locations")Set<String> locationNames ,
                                     @RequestParam(value = "minSalary") String minSal,
                                     @RequestParam(value = "maxSalary") String maxSal,
                                     HttpSession session,
                                     Pageable pageable){
        ModelMap responseMap = new ModelMap();
        try {

            if (session.getAttribute("email") == null) {
                responseMap.addAttribute("statusCode","404");
                responseMap.addAttribute("message","Please login to search");
                return responseMap;
            }

            Integer minSalary=null,maxSalary=null;
            System.out.println("minsal length :"+minSal.length()+" maxsal length :"+maxSal.length());
            if(!minSal.equals("undefined") && minSal.length()!=0)
                minSalary= Integer.parseInt(minSal);
            if(!maxSal.equals("undefined") && maxSal.length()!=0)
                maxSalary=Integer.parseInt(maxSal);

            Page<Opening> openings=openingService.searchOpeningsWithFilters(query,keywords,companyNames,locationNames,minSalary,maxSalary,pageable);

            if(openings!=null){
                List<Opening> openingsList=new ArrayList<>();
                List<String> imageURLs=new ArrayList<>();
                for(Opening o : openings){
//                    imageMap.put(o,o.getCompany().getImageUrl());
                    openingsList.add(o);
                    imageURLs.add(o.getCompany().getImageUrl());
                }

                responseMap.addAttribute("openings",openingsList);
                responseMap.addAttribute("imageURLs",imageURLs);
                responseMap.addAttribute("message","Search Results-");
                responseMap.addAttribute("statusCode","200");
                return responseMap;
            }
            else{
                responseMap.addAttribute("message","Your search returned no results");
                responseMap.addAttribute("statusCode","405");
                return responseMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseMap.addAttribute("message","Snap! Something went wrong. Please try again.");
            responseMap.addAttribute("statusCode","404");
            return responseMap;
        }

    }
    @GetMapping(value = "/verify")
    public String verify(@RequestParam(value = "key") String hashValue){
        Applicant applicant=null;
        try {
            applicant = applicantService.getApplicantWithHashValue(hashValue);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(applicant!=null){
            applicant.setVerified(true);
            try{
                applicantService.save(applicant);
                return "Congrats! Your account is now verified! Please sign in to continue";
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return "Sorry could not find you. Please try to register again!";
    }

    @PostMapping(value = "/apply",produces="application/json")
    @ResponseBody
    public ModelMap apply(@RequestBody String applicantJSON, HttpSession session){

        ModelMap responseMap = new ModelMap();
        if(session.getAttribute("email")==null) {
            System.out.println("Please login to apply");
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message","Please sign out before registering.");
        }
        try {
            System.out.println("Job ID:"+Integer.parseInt(applicantJSON.toString()));
            int statusCode=applicantService.apply(Integer.parseInt(applicantJSON.toString()), session.getAttribute("email").toString());
            if(statusCode==200){
                responseMap.addAttribute("statusCode", "200");
                responseMap.addAttribute("message","Job application submitted successfully!");
                return responseMap;
            }else
            if(statusCode==401){
                responseMap.addAttribute("statusCode", "401");
                responseMap.addAttribute("message","This application is previously submitted and is not in terminal state.");
                return responseMap;
            }else

            if(statusCode==402){
                responseMap.addAttribute("statusCode", "402");
                responseMap.addAttribute("message","You cannot have more than five pending applications.");
                return responseMap;
            }
            else {
                responseMap.addAttribute("statusCode", "404");
                responseMap.addAttribute("message","Snap! Something went wrong. Please try again later.");
                return responseMap;
            }

        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("statusCode", "405");
            return responseMap;
        }
    }

    @PostMapping(value = "/applyWithResume")
    @ResponseBody
    public ModelMap applyWithResume(@RequestBody String jsonObj,HttpSession session) throws JSONException{


        ModelMap responseMap = new ModelMap();
        try {
        	JSONObject obj = new JSONObject(jsonObj);
            System.out.println("Resume after parsing:"+obj.getString("resumeUrl"));
            System.out.println("Id after parsing:"+obj.getInt("opening_id"));
            String resume = obj.getString("resumeUrl");
            Integer openingid = obj.getInt("opening_id");

            if(session.getAttribute("email")==null){
                responseMap.addAttribute("message","Please sign in to apply");
                responseMap.addAttribute("statusCode","404");
                return responseMap;
            }

            int statusCode=applicantService.applyWithResume(resume,openingid,session.getAttribute("email").toString());
            if(statusCode==200){
                responseMap.addAttribute("statusCode", "200");
                responseMap.addAttribute("message","Job application submitted successfully!");
                return responseMap;
            }
            else
            if(statusCode==401){
                responseMap.addAttribute("statusCode", "401");
                responseMap.addAttribute("message","This application is previously submitted and is not in terminal state.");
                return responseMap;
            }
            else
            if(statusCode==402){
                responseMap.addAttribute("statusCode", "402");
                responseMap.addAttribute("message","You cannot have more than five pending applications.");
                return responseMap;
            }
            else {
                responseMap.addAttribute("statusCode", "404");
                responseMap.addAttribute("message","Snap! Something went wrong. Please try again later.");
                return responseMap;
            }
//            if(applicantService.applyWithResume(resume,openingid,session.getAttribute("email").toString())){
//                responseMap.addAttribute("statusCode","200");
//                responseMap.addAttribute("message","Congrats! Your resume has been submitted");
//                return responseMap;
//            }
//            else{
//                responseMap.addAttribute("statusCode","400");
//                responseMap.addAttribute("message","The decision for this application has been made");
//                return responseMap;
//            }
        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("statusCode","500");
            responseMap.addAttribute("message","Snap! Somethinig went wrong. Please try again.");
            return responseMap;
        }
    }

    @PostMapping(value = "/register", produces="application/json")
    @ResponseBody
    public ModelMap register(@RequestBody String applicantJSON,
                           HttpSession session){
    	ObjectMapper mapper = new ObjectMapper();
    	ModelMap responseMap = new ModelMap();
    	try{
    		Applicant applicantObject = mapper.readValue(applicantJSON, Applicant.class);
//    		System.out.println(applicantObject.getEmail());


	        if(session.getAttribute("email")!=null)
	        {
	        	responseMap.addAttribute("statusCode", "400");
                responseMap.addAttribute("message","Please sign out before registering.");
                return responseMap;
	        }

	        Applicant applicant=new Applicant();
	        applicant.setEducation(applicantObject.getEducation());
	        applicant.setSkills(applicantObject.getSkills());
	        applicant.setExperience(applicantObject.getExperience());
	        applicant.setEmail(applicantObject.getEmail());
	        applicant.setPassword(applicantObject.getPassword());
	        applicant.setLastName(applicantObject.getLastName());
	        applicant.setFirstName(applicantObject.getFirstName());
	        if(!applicantService.register(applicant)){
	           	responseMap.addAttribute("statusCode", "400");
	           	responseMap.addAttribute("message", "Email already in use! Please try with a new email.");
	        }else{
//	           session.setAttribute("email",applicantObject.getEmail());
	           responseMap.addAttribute("statusCode", "200");
	        }

	     }
	     catch (Exception e){
	         e.printStackTrace();
	         session.invalidate();
	         responseMap.addAttribute("statusCode", "400");
	         responseMap.addAttribute("message", "Snap! Something went wrong please try again later");
	     }
	     return responseMap;
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public ModelMap update(@RequestBody String json, HttpSession session) {

        ObjectMapper mapper = new ObjectMapper();
        ModelMap responseMap = new ModelMap();

        if(session.getAttribute("email")==null){
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message","Please sign in before updating.");
            return responseMap;
        }
        try{
            Applicant reqObj = mapper.readValue(json, Applicant.class);
            Applicant applicant=applicantService.fetch(session.getAttribute("email").toString());

            if(applicant==null){
                responseMap.addAttribute("statusCode", "400");
                responseMap.addAttribute("message", "Email could not be found. Make sure you are registered.");
                return responseMap;
            }


            System.out.println("intro: "+reqObj.getIntroduction());


            if(reqObj.getFirstName()!=null)
                applicant.setFirstName(reqObj.getFirstName());
            if(reqObj.getLastName()!=null)
                applicant.setLastName(reqObj.getLastName());
            if(reqObj.getIntroduction()!=null)
                applicant.setIntroduction(reqObj.getIntroduction());
            if(reqObj.getExperience()!=null)
                applicant.setExperience(reqObj.getExperience());
            if(reqObj.getSkills()!=null)
                applicant.setSkills(reqObj.getSkills());
            if(reqObj.getEducation()!=null)
                applicant.setEducation(reqObj.getEducation());
            if(reqObj.getImageUrl()!=null)
                applicant.setImageUrl(reqObj.getImageUrl());
//            if(reqObj.getResume()!=null)
//                applicant.setEducation(reqObj.getResume());
            applicantService.save(applicant);

        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message", "Snap! Something went wrong please try again later");
            return responseMap;
        }
        responseMap.addAttribute("statusCode", "200");
        return responseMap;
    }

    @PostMapping(value = "/signin")
    @ResponseBody
    public ModelMap signin(@RequestBody String json, HttpSession session){
    	ObjectMapper mapper = new ObjectMapper();
    	ModelMap responseMap = new ModelMap();
        try{
        	Applicant obj = mapper.readValue(json, Applicant.class);
//            System.out.println("Email :"+obj.getEmail());
            Applicant applicant=applicantService.fetch(obj.getEmail());

            if(applicant!=null && applicant.getPassword().equals(obj.getPassword())){
                if(!applicant.isVerified()){
                	responseMap.addAttribute("statusCode", "400");
    	           	responseMap.addAttribute("message", "Please verify your account before signing in.");
                    return responseMap;
                }
                if(session.getAttribute("email")!=null){
                	responseMap.addAttribute("statusCode", "400");
    	           	responseMap.addAttribute("message", "You are already signed in");
                    return responseMap;
                }
                session.setAttribute("email",applicant.getEmail());
                responseMap.addAttribute("statusCode", "200");
            }
            else{
            	responseMap.addAttribute("statusCode", "400");
           		responseMap.addAttribute("message", "Username or password incorrect!");
                return responseMap;
           	}
        }
        catch (Exception e){
            e.printStackTrace();
        	responseMap.addAttribute("statusCode", "400");
	        responseMap.addAttribute("message", "Snap! Something went wrong please try again later");
            session.invalidate();
        }
        return responseMap;
    }

    @PostMapping(value = "/cancelApplications")
    @ResponseBody
    public ModelMap cancelApplications(@RequestBody List<Integer>  ApplicationIds, HttpSession session){
        ModelMap responseMap = new ModelMap();
        if(session.getAttribute("email")==null){
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message", "You are not signed in");
            return responseMap;
        }
        try {


            System.out.println("Applications to cancel:"+ApplicationIds.size());
            for(int ApplicationId:ApplicationIds) {
                Application application = applicationService.getApplication(ApplicationId);
                if (applicationService.cancelApplication(application)) {
                    System.out.println(" You have successfully canceled this application with id: "+ApplicationId);
                    continue;
                }else{
                    System.out.println( "Could not cancel application already in terminal state. Application id: "+ApplicationId );
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("statusCode", "405");
            responseMap.addAttribute("message", "Exception occurred");
            return responseMap;
        }

        responseMap.addAttribute("statusCode","200");
        responseMap.addAttribute("message","Applications Withdrawn");
        return responseMap;

    }

//    public String cancelApplications(@RequestParam(value = "applicationIds") List<Integer> ApplicationIds, HttpSession session){
//        if(session.getAttribute("email")==null){
//            return "<h1>You need to sign in first!</h1>";
//        }
//
//        try {
//            for(int ApplicationId:ApplicationIds) {
//                Application application = applicationService.getApplication(ApplicationId);
//                if (applicationService.cancelApplication(application)) {
//                    System.out.println("<h1> You have successfully canceled this application </h1>");
//                    continue;
//                }
//                System.out.println( "Could not cancel application already in terminal state" );
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return "<h1> Could not cancel application at this time. Please try again</h1>";
//        }
//        return "Empty list";
//    }

//    @GetMapping(value = "/getApplications")
//    @ResponseBody
//    public ModelMap getApplications(ModelMap model,HttpSession session){
//        //System.out.println("Trying to get the applicant with email :"+email);
//        ModelMap map = new ModelMap();
//        if(session.getAttribute("email")==null) {
//            System.out.println("Not signed in");
//            map.addAttribute("statusCode", "400");
//            return map;
//        }
//
//        try {
//            Applicant applicant=applicantService.fetch(session.getAttribute("email").toString());
//            System.out.println("Found applicant "+applicant.toString());
//            List<ModelMap> main_list = new ArrayList<>();
//
//
//
//            for(Application app : applicant.getApplications()){
//                ModelMap imap = new ModelMap();
//                imap.addAttribute("opening",app.getOpening());
//                imap.addAttribute("companyName",app.getOpening().getCompany().getName());
//                imap.addAttribute("openingurl",app.getOpening().getCompany().getImageUrl());
//                imap.addAttribute("applicationStatus",app.getStatus());
//                imap.addAttribute("terminalStatus",app.isTerminal());
//                imap.addAttribute("application_id",app.getApplication_id());
//                imap.addAttribute("apply_date",app.getDate());
//                main_list.add(imap);
//
//
//            }
//            map.addAttribute("applications",main_list);
//            map.addAttribute("statusCode", "200");
//            return map;
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            map.addAttribute("statusCode", "400");
//            return map;
//        }
//    }

    @PostMapping(value = "/acceptOffer")
    @ResponseBody
    public ModelMap acceptApplications(@RequestBody List<Integer> applicationIds ,HttpSession session){
        ModelMap responseMap = new ModelMap();
        if(session.getAttribute("email")==null){
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message", "You are not signed in");
            return responseMap;
        }
        try{
            for(int applicationId:applicationIds) {
                Application application = applicationService.getApplication(applicationId);

                if (applicationService.acceptOffer(application)){
                    openingService.setStatus(application.getOpening(),Opening.Status.FILLED);
                    System.out.println("You have successfully accepted this application offer");
                    List<Application> applications=application.getOpening().getApplications();

                    for(Application app:applications){
                        System.out.println("application id :"+app.getApplication_id());
                        if( app!=application && !applicationService.cancelApplication(app)){
                            System.out.println("The application for applicant :"+app.getApplicant().getFirstName()+" is already in terminal state!");
                            continue;
                        }
                    }

                }
                else
                    System.out.println("Sorry! Could not accept application in Terminal state or an offer which was not made");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("statusCode", "405");
            responseMap.addAttribute("message", "Exception occurred");
            return responseMap;
        }

        responseMap.addAttribute("statusCode","200");
        responseMap.addAttribute("message","Offer Accepted");
        return responseMap;
    }

    @PostMapping(value = "/rejectApplications")
    @ResponseBody
    public ModelMap rejectApplications(@RequestBody  List<Integer> applicationIds, HttpSession session){
        ModelMap responseMap = new ModelMap();
        if(session.getAttribute("email")==null){
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message", "You are not signed in");
            return responseMap;
        }
        try{
            for(int applicationId:applicationIds) {
                Application application = applicationService.getApplication(applicationId);
                if (applicationService.rejectOffer(application)) {
                    System.out.println("You have successfully rejected this application offer");
                    continue;
                }
                System.out.println("Sorry! Could not reject application in Terminal state or an offer which was not made");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("statusCode", "405");
            responseMap.addAttribute("message", "Exception occurred");
            return responseMap;
        }
        responseMap.addAttribute("statusCode","200");
        responseMap.addAttribute("message","Offer Rejected");
        return responseMap;
    }




    @GetMapping(value = "/getApplications")
    @ResponseBody
    public ModelMap getApplications(ModelMap model,HttpSession session){
        //System.out.println("Trying to get the applicant with email :"+email);
        ModelMap map = new ModelMap();
        if(session.getAttribute("email")==null) {
            System.out.println("Not signed in");
            map.addAttribute("statusCode", "400");
            return map;
        }

        try {
            Applicant applicant=applicantService.fetch(session.getAttribute("email").toString());
            System.out.println("Found applicant "+applicant.toString());
            List<ModelMap> main_list = new ArrayList<>();



            for(Application app : applicant.getApplications()){
                ModelMap imap = new ModelMap();
                imap.addAttribute("opening",app.getOpening());
                imap.addAttribute("companyName",app.getOpening().getCompany().getName());
                imap.addAttribute("openingurl",app.getOpening().getCompany().getImageUrl());
                imap.addAttribute("applicationStatus",app.getStatus());
                imap.addAttribute("terminalStatus",app.isTerminal());
                imap.addAttribute("application_id",app.getApplication_id());
                imap.addAttribute("apply_date",app.getDate());
                main_list.add(imap);


            }
            map.addAttribute("applications",main_list);
            map.addAttribute("statusCode", "200");
            return map;
        }
        catch(Exception e){
            e.printStackTrace();
            map.addAttribute("statusCode", "400");
            return map;
        }
    }
    
    @PostMapping(value = "/logout")
    @ResponseBody
    public ModelMap logout(HttpSession session){
        session.invalidate();
        ModelMap responseMap = new ModelMap();
        responseMap.addAttribute("statusCode", "200");
        return responseMap;
    }

    @GetMapping(value = "/isLoggedIn")
    @ResponseBody
    public ModelMap isLoggedIn( HttpSession session){
        ModelMap responseMap = new ModelMap();
        try{
            System.out.println("In is logged in: "+session.getAttribute("email"));
            if(session.getAttribute("email")!=null){
                System.out.println("Inside session found ");
                responseMap.addAttribute("statusCode", "200");
                responseMap.addAttribute("email", session.getAttribute("email"));
            }else {
                System.out.println("Inside session not found");
                responseMap.addAttribute("statusCode", "400");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseMap;
    }

    @PostMapping(value="/interested")
    @ResponseBody
    public ModelMap markInterested(HttpSession session,@RequestBody String jsonObj){
        ModelMap responseMap = new ModelMap();
        JSONObject obj = new JSONObject(jsonObj);

        int opening_id = obj.getInt("opening_id");
        String add = obj.getString("add");

        System.out.println("opening_id:"+opening_id);
        System.out.println("add:"+add);

        if(session.getAttribute("email")==null){
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message","Please sign in before updating.");
            return responseMap;
        }
        try {
            Opening opening=openingService.getOpening(opening_id);
            System.out.println("opening_id :"+opening_id+" add :"+add);
            if(opening==null){
                responseMap.addAttribute("statusCode","404");
                responseMap.addAttribute("message","This opening id does not exist! Please try again.");
                return responseMap;
            }

            Applicant applicant=applicantService.fetch(session.getAttribute("email").toString());
            if(add.equals("true"))
            {
                applicant.addToInterestedJobs(opening);
                responseMap.addAttribute("statusCode","200");
                responseMap.addAttribute("message","Marked as interested!");
                applicantService.save(applicant);
            }
            else
            if(applicant.getInterestedOpenings().contains(opening)) {
                applicant.getInterestedOpenings().remove(opening);
                responseMap.addAttribute("statusCode","200");
                responseMap.addAttribute("message","Removed from interested!");
                applicantService.save(applicant);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseMap;
    }

    @GetMapping(value="/interview")
    @ResponseBody
    public ModelMap getInterviews(HttpSession session){
        ModelMap responseMap = new ModelMap();

        if (session.getAttribute("email") == null) {
            responseMap.addAttribute("statusCode", "401");
            responseMap.addAttribute("message", "Please sign in before updating opening.");
        }

        try {

            Applicant applicant=applicantService.fetch(session.getAttribute("email").toString());
            if(applicant==null){
                responseMap.addAttribute("statusCode", "401");
                responseMap.addAttribute("message", "Something went wrong. Please try again.");
            }
            Set<Application> applications=applicant.getApplications();
            List<Application> scheduledInterviews=new ArrayList<>();
            List<Opening> openings=new ArrayList<>();

            for(Application application:applications){
                if(application.getInterviewSchedule()!=null) {
                    openings.add(application.getOpening());
                    scheduledInterviews.add(application);
                }
            }

            responseMap.addAttribute("statusCode", "200");
            responseMap.addAttribute("message", "Scheduled interviews found successfully");
            responseMap.addAttribute("openings",openings);
            responseMap.addAttribute("scheduledInterviews",scheduledInterviews);
        }
        catch (Exception e){
            e.printStackTrace();
            responseMap.addAttribute("statusCode", "404");
            responseMap.addAttribute("message", "Snap! Something went wrong. Please try again.");
        }
        return responseMap;
    }

    @PostMapping(value="/interview")
    @ResponseBody
//    public ModelMap scheduleInterview(HttpSession session,@RequestParam(value = "application_id") Integer application_id,@RequestParam(value = "accept") String accept) {
    public ModelMap scheduleInterview(HttpSession session,@RequestBody String jsonObj) {
        ModelMap responseMap = new ModelMap();



        if (session.getAttribute("email") == null) {
            responseMap.addAttribute("statusCode", "400");
            responseMap.addAttribute("message", "Please sign in before updating.");
            return responseMap;
        }
        try {
            JSONObject obj = new JSONObject(jsonObj);
            int application_id = obj.getInt("application_id");
            String accept = obj.getString("accept");

            Application application = applicationService.getApplication(application_id);
            if(application==null){
                responseMap.addAttribute("statusCode","404");
                responseMap.addAttribute("message","Application not found.");
                return responseMap;
            }
            InterviewSchedule interviewSchedule=application.getInterviewSchedule();
            if(interviewSchedule.isTerminated()){
                responseMap.addAttribute("statusCode","401");
                responseMap.addAttribute("message","Interview is already terminated!");
                return responseMap;
            }
            if(accept.equals("true")) {
                    if(interviewSchedule.isApplicantConfirmed()){
                        responseMap.addAttribute("statusCode","404");
                        responseMap.addAttribute("message","Interview is already confirmed!");
                        return responseMap;
                    }
                interviewSchedule.setApplicantConfirmed(true);
                interviewSchedule.setTerminated(true);
                applicationService.save(application);
                responseMap.addAttribute("statusCode","200");
                responseMap.addAttribute("message","Interview schedule accepted.");
                return responseMap;
            }
            else{
                if(interviewSchedule.isApplicantConfirmed()){
                    responseMap.addAttribute("statusCode","404");
                    responseMap.addAttribute("message","Interview is already confirmed!");
                    return responseMap;
                }
                interviewSchedule.setApplicantConfirmed(false);
                interviewSchedule.setTerminated(true);
                applicationService.save(application);
                responseMap.addAttribute("statusCode","200");
                responseMap.addAttribute("message","Interview schedule rejected.");
                return responseMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseMap.addAttribute("statusCode","404");
            responseMap.addAttribute("message","Snap! Something went wrong.");
        }
        return responseMap;
    }

}
