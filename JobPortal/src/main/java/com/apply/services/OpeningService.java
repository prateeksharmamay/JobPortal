package com.apply.services;

import com.amazonaws.services.apigateway.model.Op;
import com.apply.entities.Application;
import com.apply.entities.Company;
import com.apply.entities.Opening;
import com.apply.repositories.OpeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Vivek Agarwal on 5/12/2017.
 */
@Service
public class OpeningService {

    @Autowired
    private OpeningRepository openingRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private EntityManager em;

    public Opening getOpening(int id) throws  Exception{
            return openingRepository.findOne(id);
    }

    public Set<Opening> getOpenings(String email) throws Exception{
        return openingRepository.findByEmail(email);
    }

    public Opening postOpening(Opening opening) throws Exception{
        openingRepository.save(opening);
        return opening;
    }

    public void deleteOpening(int id) throws Exception{
        openingRepository.delete(id);
    }

    public void flushOpenings(String email) throws Exception{
        openingRepository.flushOpeningsForCompany(email);
    }

    public Page<Opening> getAllOpenings(Pageable pageable) {
        return openingRepository.findAll(pageable);
    }

    public void update(Opening opening) throws Exception{
        openingRepository.save(opening);
    }

    public Opening getOpeningForCompany(int opening_id, String email) throws Exception{
        return openingRepository.findByIdAndCompany(opening_id,email);
    }

    public Page<Opening> searchOpenings(String query,Pageable pageable) {
        String words[]=query.split("\\s+");
        LinkedHashSet<Opening> openingSet=new LinkedHashSet<>();
        List<Opening> current;

        for(String word : words) {
            System.out.println("Current word :"+word);
            current = openingRepository.getSearchResults(word);
            System.out.println("Result for search :"+current);
            for(Opening opening:current){
                openingSet.add(opening);
            }
        }

        List<Opening> openings=new ArrayList<>();
        for(Opening opening:openingSet){
            openings.add(opening);
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > openings.size() ? openings.size() : (start + pageable.getPageSize());
        Page<Opening> pages = new PageImpl<Opening>(openings.subList(start,end), pageable, openings.size());
        return pages;
    }

    public List<Opening> searchOpenings(String query) {
        String words[]=query.split("\\s+");
        LinkedHashSet<Opening> openingSet=new LinkedHashSet<>();
        List<Opening> current;

        for(String word : words) {
            System.out.println("Current word :"+word);
            current = openingRepository.getSearchResults(word);
            System.out.println("Result for search :"+current);
            for(Opening opening:current){
                openingSet.add(opening);
            }
        }

        List<Opening> openings=new ArrayList<>();
        for(Opening opening:openingSet){
            openings.add(opening);
        }
        return openings;
    }

    public Page<Opening> searchOpeningsWithFilters(String query,Set<String> keywordsSet, Set<String> companyNamesSet, Set<String> locationNamesSet, Integer minSalary, Integer maxSalary, Pageable pageable) {
        List<Opening> queryResults=searchOpenings(query);
        System.out.println("Query Result :"+queryResults);
        List<Opening> openings=new ArrayList<>();
        Set<String> companyNames = new HashSet<>();
        Set<String> locationNames=new HashSet<>();
        Set<String> keywords=new HashSet<>();

        for(String companyName:companyNamesSet){

                if(!companyName.equals("undefined"))
                    companyNames.add(companyName.toLowerCase().trim());
        }

        for(String locationName:locationNamesSet){
            if(!locationName.equals("undefined"))
                locationNames.add(locationName.toLowerCase().trim());
        }

        for(String keyword:keywordsSet){
            if(!keyword.equals("undefined"))
                keywords.add(keyword.toLowerCase().trim());
        }


        for(Opening opening:queryResults){
//
//
//            if((int)minSalary==(int)maxSalary && (int)minSalary>=(int)opening.getMinSalary() && (int)maxSalary<=(int)opening.getMaxSalary()){
//                System.out.println("Single point salary condition satisfied for opening :"+opening.getTitle());
//            }
            if((keywords.isEmpty()|| hasKeyWord(opening.getDescription(),keywords) || hasKeyWord(opening.getResponsibilities(),keywords) || hasKeyWord(opening.getTitle(),keywords))
                    && (companyNames.isEmpty() || companyNames.contains(opening.getCompany().getName().toLowerCase()))
                    && (locationNames.isEmpty() || locationNames.contains(opening.getLocation().toLowerCase()))
                    && ((minSalary==null && maxSalary==null)||(minSalary==null && maxSalary==opening.getMaxSalary())||(maxSalary==null && minSalary==opening.getMinSalary())||((int)minSalary<=opening.getMinSalary() && (int)maxSalary>=opening.getMaxSalary())||((int)minSalary==(int)(maxSalary) && (int)minSalary>=(int)opening.getMinSalary() && (int)maxSalary<=(int)opening.getMaxSalary()))
                    ){
                System.out.println("Inside enter openings");
                openings.add(opening);
            }
        }

        int start = pageable.getOffset();
        System.out.println("page offset:"+start);
        System.out.println("pagezise:"+pageable.getPageSize());
        System.out.println("openig size:"+openings.size());
        int end = (start + pageable.getPageSize()) > openings.size() ? openings.size() : (start + pageable.getPageSize());
        System.out.println("end :"+end);
        Page<Opening> pages = new PageImpl<Opening>(openings.subList(start,end), pageable, openings.size());
        return pages;
    }

    private boolean hasKeyWord(String description, Set<String> keywords) {
        for(String keyword:keywords){
            String words[]=description.split("\\s+");
            for(String word:words){
                String brokenKeys[]=keyword.split("\\s+");
                for(String key:brokenKeys) {
                    if (word.toLowerCase().trim().equals(key.toLowerCase().trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean cancelOpening(Opening opening) throws Exception{
        opening.setStatus(Opening.Status.CANCELLED);
        return true;
    }

    public boolean setStatus(Opening opening, Opening.Status status){
        if(opening!=null){
            opening.setStatus(status);
            return true;
        }
        return false;
    }
}
