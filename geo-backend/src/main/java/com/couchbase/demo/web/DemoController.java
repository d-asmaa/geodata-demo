package com.couchbase.demo.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.java.util.Coordinate;
import com.couchbase.demo.data.CoordinateListRepository;
import com.couchbase.demo.domain.CBCoordinate;

@RestController
public class DemoController {
    private final CoordinateListRepository repo;

    /**
     * This constructor receives a value that is a Spring bean and is automatically
     * dependency injected during the lifecycle processing of the Spring container.
     *
     * @param repo The instance of DemoListRepository that is discovered by component scanning
     *             and initialized as a Spring bean
     */
    public DemoController(CoordinateListRepository repo) {
        this.repo = repo;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value = "/coordinates")
    public List<CBCoordinate> controllerMethod(@RequestParam Map<String, String> customQuery) {
        String startDate = customQuery.get("startDate");
        String endDate = customQuery.get("endDate");
        String pointsString = customQuery.get("points");
        System.out.println("customQuery = startDate " + startDate);
        System.out.println("customQuery = endDate " + endDate);
        
        //** Convert the input to coordinate list
        pointsString = pointsString.substring(1, pointsString.length() - 1);
        List<String> coordinates = new ArrayList<String>(Arrays.asList(pointsString.split(Pattern.quote("),("))));
        
        //List polygon  = new List() {};
        List<Coordinate> points = new ArrayList<Coordinate>();
        coordinates.forEach((c) -> {

            points.add(Coordinate.ofLonLat(Double.parseDouble(c.split(",")[0]),Double.parseDouble(c.split(",")[1])));
        });
        return repo.getFTSData(startDate,endDate, points);
    }


}
