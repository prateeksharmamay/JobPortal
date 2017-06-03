package com.apply.aws;



        import com.amazonaws.services.s3.model.PutObjectResult;
        import com.amazonaws.services.s3.model.S3ObjectSummary;
        import com.apply.aws.S3Wrapper;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;
        import org.springframework.web.multipart.MultipartFile;

        import java.io.IOException;
        import java.util.List;

@RestController
@RequestMapping("/api/aws/s3")
public class UploadController {

    @Autowired
    private S3Wrapper s3Wrapper;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public List<PutObjectResult> upload(@RequestParam("file") MultipartFile[] multipartFiles) {

        List<PutObjectResult> p = s3Wrapper.upload(multipartFiles);

        System.out.println("return messages3:"+p.toString());
        System.out.println("return size:"+p.size());

            return p;


    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@RequestParam String key) throws IOException {
        return s3Wrapper.download(key);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<S3ObjectSummary> list() throws IOException {
        System.out.println("Reached in S3 list");
        return s3Wrapper.list();
    }
}