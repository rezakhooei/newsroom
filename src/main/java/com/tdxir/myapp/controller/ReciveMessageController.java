package com.tdxir.myapp.controller;

//import com.google.gson.JsonObject;
import com.tdxir.myapp.model.Users;
import com.tdxir.myapp.nlp.training.MakeTsv;
import com.tdxir.myapp.repository.UserRepository;
import com.tdxir.myapp.service.GoogleSpeech;
import com.tdxir.myapp.service.ProccessMessage;
import com.tdxir.myapp.service.RecordAndProccessMessageService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/uploadFile")
public class ReciveMessageController {
    @Autowired
    UserRepository userRepository;

    private final RecordAndProccessMessageService recordAndProccessMessageService;
    @Autowired
    private ProccessMessage proccessMessage;
    private final GoogleSpeech googleSpeech = new GoogleSpeech();
    private  MakeTsv makeTsv;
    private static final String EXTENSION = ".wav";
    private static final String SERVER_LOCATION = "/opt/tomcat/uploads";

    public ReciveMessageController(RecordAndProccessMessageService recordAndProccessMessageService, ProccessMessage proccessMessage) {

        this.recordAndProccessMessageService = recordAndProccessMessageService;
        this.proccessMessage = proccessMessage;
    }

    @PostMapping
    public ResponseEntity<JSONObject> uploadFile(
            @RequestParam(name = "fileSound", required = false) MultipartFile fileSound,@RequestParam(name = "filePic", required = false) MultipartFile filePic,
            @RequestParam("inf1") String inf1, @RequestParam("inf2") String inf2,@RequestParam("inf3") String inf3,@RequestParam("inf4")String inf4,@RequestParam("rdButton1") String rdButton1 ) throws Exception
     {   //googleSpeech.initialize();


         List<String> inf = null;

         inf.add(inf1);inf.add(inf2);inf.add(inf3);inf.add(inf4);
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


         Users user= userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
         String message="";
        // message= recordAndProccessMessageService.storeInfs(file, inf1, inf2, inf3, inf4);
          System.out.println(authentication.getName());


        if(!authentication.getName().equals("javadghane18@gmail.com")&&(message != "apikey not valid OR google didn't reply")) {
             JSONObject jsonObjectMain = new JSONObject();
             JSONObject jsonObject = new JSONObject();
             //message="کد 902 چند تا داریم و برای چیست و چقدر بخریم ؟";
             //List<String> processList=recordAndProccessMessageService.proccessMessage(message,user.getUserKind());


          //  ProccessMessage proccessMessage=new ProccessMessage(user.getUserKind());
            //proccessMessage.(user.getUserKind());
            List<String> processList=proccessMessage.proccess(message,user.getUserKind());
             if((processList) ==null) {
                 processList=new ArrayList<>();
                 processList.add(" پاسخی پیدا نکردم");
             }
                 JSONArray array = new JSONArray();

             for (int i = 1; i <= processList.size(); ++i) {
                 jsonObject.put("inf_id", "");//String.valueOf(i));
                 jsonObject.put("inf_text",  processList.get(i-1));
                 array.add(new JSONObject(jsonObject));
                 jsonObject.clear();
             }
             jsonObjectMain.put("inf", array);
             // array.add(jsonObject);

          /*   MakeTsv makeTsv;//=new MakeTsv(new MahakRepository()) ;
             makeTsv = new MakeTsv();
             makeTsv.createTsv(file);//.getResource().getFile());
*/


             //inf1="";
             //for (int i=0;i<=5;++i)

             String fileName="receivedmessage.wav";//"monshi.mp3";
            // inf1 = "افلاطون بیان می کند که زندگی ما در بیشتر مواقع به این خاطر با مشکل مواجه می شود که ما تقریباً هیچ وقت فرصت کافی به خودمان نمی دهیم تا به شکلی دقیق و عاقلان افلاطون قصد داشت تا نظم و شفافیت را در ذهن مخاطبینش به وجود آورد";
             UploadResponse uploadResponse = new UploadResponse(fileName,fileName, inf);

             String image = fileName;//"file";
             File filereply = new File(SERVER_LOCATION + File.separator + image);//+ EXTENSION);

             HttpHeaders header = new HttpHeaders();
             header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filereply");//monshi.mp3");

             header.add("Cache-Control", "no-cache, no-store, must-revalidate");
             header.add("Pragma", "no-cache");
             header.add("Expires", "0");

             Path path = Paths.get(filereply.getAbsolutePath());
             ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

             byte[] encoder = Base64.getEncoder().encode(resource.getByteArray());

             jsonObjectMain.put("file_content", resource.getByteArray());
             // array.add(new JSONObject(jsonObject));
             // jsonObject.clear();

             InputStream is = new ByteArrayInputStream(encoder);
             InputStreamResource resource1 = new InputStreamResource(is);

             HttpHeaders headers = new HttpHeaders();
             headers.setContentType(MediaType.APPLICATION_JSON);

             // ContentDisposition disposition = ContentDisposition.attachment().filename("monshi.mp3").build();
             ContentDisposition disposition = ContentDisposition.attachment().filename(filereply.getName()).build();
             headers.setContentDisposition(disposition);

             return new ResponseEntity<>(jsonObjectMain, headers, HttpStatus.OK);


         }
















         else if(authentication.getName().equals("javadghane18@gmail.com")){
             JSONObject jsonObjectMain = new JSONObject();
             JSONObject jsonObject = new JSONObject();


             JSONArray array = new JSONArray();

             for (int i = 1; i <= 3; ++i) {
                 jsonObject.put("inf_id", String.valueOf(i));
                 jsonObject.put("inf_text", String.valueOf(i) + "افلاطون بیان می کند که زندگی ما در بیشتر مواقع به این خاطر با مشکل مواجه می شود که ما تقریباً هیچ وقت فرصت کافی به خودمان نمی دهیم تا به شکلی دقیق و عاقلانه به تصمیمات مان فکر کنیم. و به همین دلیل، ارزش ها، روابط و شغل هایی نامناسب نصیب مان می شود. افلاطون قصد داشت تا نظم و شفافیت را در ذهن مخاطبینش به وجود آورد. او دریافته بود که بسیاری از نظرات و قضاوت های ما از تفکرات جامعه نشأت می گیرد، از چیزی که یونانی ها آن را «دوکسا» یا «عقل جمعی حاکم» می نامند. افلاطون در 36 کتابی که نوشت، بارها و بارها نشان داد که این «عقل جمعی حاکم» می تواند پر از اشتباه، تبعیض و خرافه باشد و تفکرات شایع در مورد عشق، شهرت، پول و یا خوبی، چندان با منطق و واقعیت همخوانی ندارن" +
                         "افلاطون همچنین دریافته بود که چگونه انسان های مغرور، تحت سلطه ی غرایز و احساسات خود هستند و آن ها را با افرادی مقایسه می کرد که توسط اسب هایی وحشی که چشم هایشان پوشانده شده، به این سو و آن سو کشیده می شوند.رویای موجود در پسِ مفهوم عشق این است که می توانیم با نزدیک شدن به چنین افرادی، اندکی مانند آن ها شویم. عشق در نظر افلاطون، نوعی آموزش است. او عقیده دارد کسی که به معنای واقعی کلمه به فردی دیگر عشق می ورزد، تمایل خواهد داشت که توسط معشوق خود به فرد بهتری تبدیل شود. این یعنی شخص باید با کسی باشد که بخشی گمشده از هستی او را در اختیار دارد: ویژگی های خوبی که خودمان از آن ها بی بهره ایم." +
                         "افلاطون از ما می خواهد این را بپذیریم که کامل نیستیم و تمایل داشته باشیم که ویژگی های خوب دیگران را در خودمان پرورش دهیم. ");
                 array.add(new JSONObject(jsonObject));
                 jsonObject.clear();
             }
             jsonObjectMain.put("inf", array);
             // array.add(jsonObject);

            if(rdButton1=="ImageANdVoice") {

                String fileName = recordAndProccessMessageService.storeInfs(fileSound, filePic, inf);
                fileName = "monshi.mp3";
                inf.add(0, "افلاطون بیان می کند که زندگی ما در بیشتر مواقع به این خاطر با مشکل مواجه می شود که ما تقریباً هیچ وقت فرصت کافی به خودمان نمی دهیم تا به شکلی دقیق و عاقلان افلاطون قصد داشت تا نظم و شفافیت را در ذهن مخاطبینش به وجود آورد");
                //UploadResponse uploadResponse = new UploadResponse(fileName,fileName,inf);

                String image = fileName;//"file";
                File filereply = new File(SERVER_LOCATION + File.separator + image);//+ EXTENSION);

             /*HttpHeaders header = new HttpHeaders();
             header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filereply");//monshi.mp3");

             header.add("Cache-Control", "no-cache, no-store, must-revalidate");
             header.add("Pragma", "no-cache");
             header.add("Expires", "0");
*/
                Path path = Paths.get(filereply.getAbsolutePath());
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

                byte[] encoder = Base64.getEncoder().encode(resource.getByteArray());

                jsonObjectMain.put("file_content1", resource.getByteArray());


                String image1 = "img.jpg";
                File filereplyImg = new File(SERVER_LOCATION + File.separator + image1);//+ EXTENSION);

             /*HttpHeaders header = new HttpHeaders();
             header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filereply");//monshi.mp3");

             header.add("Cache-Control", "no-cache, no-store, must-revalidate");
             header.add("Pragma", "no-cache");
             header.add("Expires", "0");
*/
                Path path1 = Paths.get(filereplyImg.getAbsolutePath());
                ByteArrayResource resource1 = new ByteArrayResource(Files.readAllBytes(path1));

                byte[] encoder1 = Base64.getEncoder().encode(resource1.getByteArray());

                jsonObjectMain.put("file_content2", resource1.getByteArray());
            }
            else if(rdButton1=="Voice"){                String fileName = recordAndProccessMessageService.storeInfs(fileSound, filePic, inf);
                fileName = "monshi.mp3";
                inf.add(0, "افلاطون بیان می کند که زندگی ما در بیشتر مواقع به این خاطر با مشکل مواجه می شود که ما تقریباً هیچ وقت فرصت کافی به خودمان نمی دهیم تا به شکلی دقیق و عاقلان افلاطون قصد داشت تا نظم و شفافیت را در ذهن مخاطبینش به وجود آورد");
                //UploadResponse uploadResponse = new UploadResponse(fileName,fileName,inf);

                String image = fileName;//"file";
                File filereply = new File(SERVER_LOCATION + File.separator + image);//+ EXTENSION);

             /*HttpHeaders header = new HttpHeaders();
             header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filereply");//monshi.mp3");

             header.add("Cache-Control", "no-cache, no-store, must-revalidate");
             header.add("Pragma", "no-cache");
             header.add("Expires", "0");
*/
                Path path = Paths.get(filereply.getAbsolutePath());
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

                byte[] encoder = Base64.getEncoder().encode(resource.getByteArray());

                jsonObjectMain.put("file_content1", resource.getByteArray());

            }
            else if(rdButton1=="Image"){                String image1 = "img.jpg";
                File filereplyImg = new File(SERVER_LOCATION + File.separator + image1);//+ EXTENSION);

             /*HttpHeaders header = new HttpHeaders();
             header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filereply");//monshi.mp3");

             header.add("Cache-Control", "no-cache, no-store, must-revalidate");
             header.add("Pragma", "no-cache");
             header.add("Expires", "0");
*/
                Path path1 = Paths.get(filereplyImg.getAbsolutePath());
                ByteArrayResource resource1 = new ByteArrayResource(Files.readAllBytes(path1));

                byte[] encoder1 = Base64.getEncoder().encode(resource1.getByteArray());

                jsonObjectMain.put("file_content2", resource1.getByteArray());

            }
                //InputStream is = new ByteArrayInputStream(encoder);
                // InputStreamResource resource1 = new InputStreamResource(is);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // ContentDisposition disposition = ContentDisposition.attachment().filename("monshi.mp3").build();
                // ContentDisposition disposition = ContentDisposition.attachment().filename(filereply.getName()).build();
                //headers.setContentDisposition(disposition);

                return new ResponseEntity<>(jsonObjectMain, headers, HttpStatus.OK);




         }
         else  //////   google didn't reply
          { JSONObject jsonObjectMain = new JSONObject();
            JSONObject jsonObject = new JSONObject();


            JSONArray array = new JSONArray();
        /*array.add("element_1");
        array.add("element_2");
        array.add("element_3");*/
            for (int i = 1; i <= 3; ++i) {
                jsonObject.put("inf_id", String.valueOf(i));
                jsonObject.put("inf_text", String.valueOf(i) + "افلاطون بیان می کند که زندگی ما در بیشتر مواقع به این خاطر با مشکل مواجه می شود که ما تقریباً هیچ وقت فرصت کافی به خودمان نمی دهیم تا به شکلی دقیق و عاقلانه به تصمیمات مان فکر کنیم. و به همین دلیل، ارزش ها، روابط و شغل هایی نامناسب نصیب مان می شود. افلاطون قصد داشت تا نظم و شفافیت را در ذهن مخاطبینش به وجود آورد. او دریافته بود که بسیاری از نظرات و قضاوت های ما از تفکرات جامعه نشأت می گیرد، از چیزی که یونانی ها آن را «دوکسا» یا «عقل جمعی حاکم» می نامند. افلاطون در 36 کتابی که نوشت، بارها و بارها نشان داد که این «عقل جمعی حاکم» می تواند پر از اشتباه، تبعیض و خرافه باشد و تفکرات شایع در مورد عشق، شهرت، پول و یا خوبی، چندان با منطق و واقعیت همخوانی ندارن" +
                        "افلاطون همچنین دریافته بود که چگونه انسان های مغرور، تحت سلطه ی غرایز و احساسات خود هستند و آن ها را با افرادی مقایسه می کرد که توسط اسب هایی وحشی که چشم هایشان پوشانده شده، به این سو و آن سو کشیده می شوند.رویای موجود در پسِ مفهوم عشق این است که می توانیم با نزدیک شدن به چنین افرادی، اندکی مانند آن ها شویم. عشق در نظر افلاطون، نوعی آموزش است. او عقیده دارد کسی که به معنای واقعی کلمه به فردی دیگر عشق می ورزد، تمایل خواهد داشت که توسط معشوق خود به فرد بهتری تبدیل شود. این یعنی شخص باید با کسی باشد که بخشی گمشده از هستی او را در اختیار دارد: ویژگی های خوبی که خودمان از آن ها بی بهره ایم." +
                        "افلاطون از ما می خواهد این را بپذیریم که کامل نیستیم و تمایل داشته باشیم که ویژگی های خوب دیگران را در خودمان پرورش دهیم. ");
                array.add(new JSONObject(jsonObject));
                jsonObject.clear();
            }
            jsonObjectMain.put("inf", array);
            // array.add(jsonObject);



            String fileName = recordAndProccessMessageService.storeInfs(fileSound,filePic, inf);
            fileName="monshi.mp3";
            inf.add(0,"جوجل پاسخ نداد");
            UploadResponse uploadResponse = new UploadResponse(fileName,fileName, inf);

            String image = fileName;//"file";
            File filereply = new File(SERVER_LOCATION + File.separator + image);//+ EXTENSION);

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filereply");//monshi.mp3");

            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            Path path = Paths.get(filereply.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            byte[] encoder = Base64.getEncoder().encode(resource.getByteArray());

            jsonObjectMain.put("file_content", resource.getByteArray());
            // array.add(new JSONObject(jsonObject));
            // jsonObject.clear();

            InputStream is = new ByteArrayInputStream(encoder);
            InputStreamResource resource1 = new InputStreamResource(is);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // ContentDisposition disposition = ContentDisposition.attachment().filename("monshi.mp3").build();
            ContentDisposition disposition = ContentDisposition.attachment().filename(filereply.getName()).build();
            headers.setContentDisposition(disposition);

            return new ResponseEntity<>(jsonObjectMain, headers, HttpStatus.OK);}

    }
}