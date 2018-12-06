package com.nsa.team10.asgproject.controllers.api.v1;

import com.nsa.team10.asgproject.FilteredPageRequest;
import com.nsa.team10.asgproject.PaginatedList;
import com.nsa.team10.asgproject.repositories.daos.CandidateDao;
import com.nsa.team10.asgproject.repositories.daos.GSCourseDao;
import com.nsa.team10.asgproject.repositories.daos.GSCourseLocationDao;
import com.nsa.team10.asgproject.repositories.daos.GSCourseTypeDao;
import com.nsa.team10.asgproject.services.dtos.NewGSCourseDto;
import com.nsa.team10.asgproject.services.interfaces.IGSCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gscourses")
public class GSCourseApiController
{
    private final IGSCourseService gsCourseService;

    @Autowired
    public GSCourseApiController(IGSCourseService gsCourseService)
    {
        this.gsCourseService = gsCourseService;
    }

    @PostMapping("")
    public ResponseEntity create(@Valid @RequestBody NewGSCourseDto newGSCourse)
    {
        gsCourseService.create(newGSCourse);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/{courseId:[0-9]+}/candidates/{candidateId:[0-9]+}")
    public ResponseEntity assignCandidateToCourse(@PathVariable long courseId, @PathVariable long candidateId)
    {
        gsCourseService.assignCandidateToCourse(courseId, candidateId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{courseId:[0-9]+}/instructor/{instructorId:[0-9]+}")
    public ResponseEntity assignInstructorToCourse(@PathVariable long courseId, @PathVariable long instructorId)
    {
        gsCourseService.assignInstructorToCourse(courseId, instructorId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<PaginatedList<GSCourseDao>> findAll(@RequestParam(value = "instructorId", required = false) Long instructorId, @RequestParam(value = "page", required = false, defaultValue = "1") long page, @RequestParam(value = "pageSize", required = false, defaultValue = "10") byte pageSize, @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy, @RequestParam(value = "orderByAscending", required = false, defaultValue = "true") boolean orderByAscending, @RequestParam(value = "search", required = false, defaultValue = "") String searchTerm)
    {
        var pageRequest = new FilteredPageRequest(page, pageSize, orderBy, orderByAscending, searchTerm);
        PaginatedList<GSCourseDao> courses;
        if (instructorId == null)
            courses = gsCourseService.findAll(pageRequest);
        else
            courses = gsCourseService.findAllForInstructor(instructorId, pageRequest);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<GSCourseDao> findById(@PathVariable long id)
    {
        var course = gsCourseService.findById(id);
        if (course.isPresent())
            return new ResponseEntity<>(course.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id:[0-9]+}/candidates")
    public ResponseEntity<PaginatedList<CandidateDao>> findAssignedCandidates(@PathVariable long id, @RequestParam(value = "page", required = false, defaultValue = "1") long page, @RequestParam(value = "pageSize", required = false, defaultValue = "10") byte pageSize, @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy, @RequestParam(value = "orderByAscending", required = false, defaultValue = "true") boolean orderByAscending)
    {
        var pageRequest = new FilteredPageRequest(page, pageSize, orderBy, orderByAscending, "");
        var candidates = gsCourseService.findAssignedCandidates(id, pageRequest);
        return new ResponseEntity<>(candidates, HttpStatus.OK);
    }

    @GetMapping("/types")
    public ResponseEntity<PaginatedList<GSCourseTypeDao>> findAllTypes(@RequestParam(value = "page", required = false, defaultValue = "1") long page, @RequestParam(value = "pageSize", required = false, defaultValue = "10") byte pageSize, @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy, @RequestParam(value = "orderByAscending", required = false, defaultValue = "true") boolean orderByAscending, @RequestParam(value = "search", required = false, defaultValue = "") String searchTerm)
    {
        var pageRequest = new FilteredPageRequest(page, pageSize, orderBy, orderByAscending, searchTerm);
        var types = gsCourseService.findAllTypes(pageRequest);
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @GetMapping("/types/all")
    public ResponseEntity<List<GSCourseTypeDao>> findAllTypes()
    {
        var types = gsCourseService.findAllTypes();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<PaginatedList<GSCourseLocationDao>> findAllLocations(@RequestParam(value = "page", required = false, defaultValue = "1") long page, @RequestParam(value = "pageSize", required = false, defaultValue = "10") byte pageSize, @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy, @RequestParam(value = "orderByAscending", required = false, defaultValue = "true") boolean orderByAscending, @RequestParam(value = "search", required = false, defaultValue = "") String searchTerm)
    {
        var pageRequest = new FilteredPageRequest(page, pageSize, orderBy, orderByAscending, searchTerm);
        var locations = gsCourseService.findAllLocations(pageRequest);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/locations/all")
    public ResponseEntity<List<GSCourseLocationDao>> findAllLocations()
    {
        var locations = gsCourseService.findAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
}