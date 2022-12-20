package de.unibamberg.dsam.group6.prost.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProstErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) return "errors/404";

            if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) return "errors/500";
        }
        return "errors/error";
    }
}
