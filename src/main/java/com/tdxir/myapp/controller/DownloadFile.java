package com.tdxir.myapp.controller;

import com.tdxir.myapp.model.UsersData;
import com.tdxir.myapp.service.HistoryService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hibernate.internal.util.collections.JoinedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DownloadFile {

    private static final String EXTENSION = ".mp3";
    private static final String EXTENSION1 = ".mp3";
    private static final String SERVER_LOCATION ="/opt/tomcat/uploads";// "uploads";
    @Autowired
    HistoryService historyService;

    @RequestMapping(path = "/download", method = RequestMethod.POST)
    public ResponseEntity<JSONArray> download(/*@RequestParam String countRequest*/) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<UsersData> usersData=historyService.usersdatahistory(authentication.getName(), 1);




        //select filename from users_data where userid=userid;
        int lastrecordindex =usersData.size();
         String image;
         String userid;

        JSONArray final_array=new JSONArray();
        for(int info=1;info<=3;++info) {
            userid=usersData.get(lastrecordindex-info).getUserid();
            JSONObject jsonInfo = new JSONObject();
            JSONArray arrayInfo=new JSONArray();
            for (int id=1;id<=3;++id){
                JSONObject jsonId = new JSONObject();
                jsonId.put("inf_id"+String.valueOf(info),id);
                if(userid.equals("javadghane18@gmail.com")){

                    jsonId.put("inf_text","اطلاعات شماره "+String.valueOf(info) );
                }
                else {
                    if(id==1)
                    jsonId.put("inf_text",usersData.get(lastrecordindex - info).getInf1() );
                    else if (id==2) {
                        jsonId.put("inf_text",usersData.get(lastrecordindex - info).getInf2() );
                    } else if (id==3) {
                        jsonId.put("inf_text",usersData.get(lastrecordindex - info).getInf3() );
                    }
                }

                arrayInfo.add(jsonId);
            }
            jsonInfo.put("inf",arrayInfo);




            image = usersData.get(lastrecordindex - info).getFilename();//.indexOf(33)[u].getFilename();

            File filereply1 = new File(SERVER_LOCATION + File.separator + image);//+ EXTENSION);
            Path path1 = Paths.get(filereply1.getAbsolutePath());
            ByteArrayResource resource1 = new ByteArrayResource(Files.readAllBytes(path1));
            byte[] encoder1 = Base64.getEncoder().encode(resource1.getByteArray());


            jsonInfo.put("file_content",resource1.getByteArray());
            final_array.add(jsonInfo);



        }






        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ContentDisposition disposition = ContentDisposition.attachment().filename("monshi.mp3").build();
        headers.setContentDisposition(disposition);


        return  new ResponseEntity<>(final_array,headers,HttpStatus.OK);

    }



}

