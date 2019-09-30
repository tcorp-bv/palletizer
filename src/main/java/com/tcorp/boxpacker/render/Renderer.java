package com.tcorp.boxpacker.render;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;

import static spark.Spark.*;

public class Renderer {
    public Renderer(int port){
        staticFileLocation("/public");
        port(port);
        //cubes = "{}"
        get("/render", (req, res) -> {
            String cubes = req.queryParams("cubes");//"[{'pos':[0,0,0], 'dim':[540,420,280]}, {'pos':[0,420,0], 'dim':[100,100,100]}]"
            HashMap<String, Object> params = new HashMap<>();
            params.put("cubes", cubes);
            return new VelocityTemplateEngine().render(new ModelAndView(params, "index.vtl"));
        });
    }
}
