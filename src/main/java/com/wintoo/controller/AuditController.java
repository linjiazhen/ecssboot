package com.wintoo.controller;

import com.wintoo.model.AuditReport;
import com.wintoo.model.DataTable;
import com.wintoo.service.AuditService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Controller
public class AuditController extends BaseController {

    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "getallreports.do", method = RequestMethod.POST)
    @ResponseBody
    public DataTable getReports() {
        return auditService.getAllReports();
    }

    @RequestMapping(value = "addreport.do", method = RequestMethod.POST)
    @ResponseBody
    public void addReport(AuditReport report) {
        auditService.addReport(report);
    }

    @RequestMapping(value = "deletereport.do", method = RequestMethod.POST)
    @ResponseBody
    public void deleteReport(@RequestBody String ids) {
        auditService.deleteReport(ids);
    }

    @RequestMapping(value = "updatereport.do", method = RequestMethod.POST)
    @ResponseBody
    public void updateReport(AuditReport report) {
        auditService.updateReport(report);
    }

    @RequestMapping(value = "fileUploader.do", method = RequestMethod.POST)
    @ResponseBody
    public void upload(MultipartHttpServletRequest request, HttpSession session) throws IOException, InvalidFormatException {
        Iterator<String> itr =  request.getFileNames();
        String uuid=request.getParameter("uuid");
        while(itr.hasNext()){
            MultipartFile mpf = request.getFile(itr.next());
//            System.out.println(mpf.getOriginalFilename() +" uploaded at "+session.getServletContext().getRealPath("/"));
            String file=session.getServletContext().getRealPath("/") + "static/files/" + mpf.getOriginalFilename();
            FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(file));
            auditService.saveFile(uuid,file,mpf.getOriginalFilename());
        }
    }

    @RequestMapping(value = "{uuid}/fileDownload.do",method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable String uuid, HttpSession session) throws IOException {
        String path=session.getServletContext().getRealPath("/") + "static/files/";
        String filename;
        if(uuid.equals("template"))
            filename="审计报告模版.doc";
        else
            filename=auditService.getFile(path,uuid);
        File file=new File(path+filename);
        HttpHeaders headers = new HttpHeaders();
        String fileName=new String(filename.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }
}
