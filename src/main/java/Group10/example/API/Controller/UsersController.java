package Group10.example.API.Controller;

import Group10.example.API.Model.Admin;
import Group10.example.API.Model.Lecturer;
import Group10.example.API.Model.Student;
import Group10.example.API.Model.StudentPayload;
import Group10.example.API.Repository.AdminRepository;
import Group10.example.API.Repository.LecturerRepository;
import Group10.example.API.Repository.StudentRepository;
import Group10.example.API.Service.CourseService;
import Group10.example.API.Service.MailService;
import Group10.example.API.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@RestController
public class UsersController {

    @Autowired
    StudentRepository stuRepo;

    @Autowired
    MailService mailService;

    @Autowired
    AdminRepository adminRepo;

    @Autowired
    LecturerRepository lecRepo;

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //get student details from session
    private Student getStudentFromSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return stuRepo.findByuserName(currentUserName);
        }
        return null;
    }

    //testing aurthorization filters
    @RequestMapping("/admin")
    public String helloAdmin(){
        //take logged user username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return "hello " + name;
    }

    @RequestMapping("/student")
    public HashMap<String,Object> helloStu(){
        Student stu = getStudentFromSession();
        return studentService.getCourselist(stu);

    }

    @RequestMapping("/lecturer")
    public String helloLec(){
        //take logged user username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return "hello " + name;
    }

    //for check mail service
    @RequestMapping("admin/mail")
    public void check(){
        mailService.sendMail("e16399@eng.pdn.ac.lk","check","test");

    }


    @PostMapping(value="/admin/registration/student",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String,Object> registerStudent(@Valid @RequestBody Student student)
    {
        HashMap<String,Object> map = new HashMap<>();
        String RegNum = student.getRegNumber();
        student.setUserName(RegNum);
        Student stud = stuRepo.findByuserName(student.getUserName());

        //check whether user is already exists
        if(stud != null) {
            map.put("msg","user Name is already exists Try with different one");
            return map;
        }

        student.setRole("STUDENT");
        String pass = studentService.passGenerate();
        student.setPassword(passwordEncoder.encode(pass));

        stuRepo.save(student);
        String mail = student.getEmail();
        String password ="Password: " + pass;
        String name = "User Name: " + student.getUserName();
        String body = name+ "      " + password;
        //sendmail
        mailService.sendMail(mail,body  ,"Account Creation");

        //successfully registered and return the registered user
        map.put("Student",stuRepo.findByuserName(student.getUserName()));
        return map;
    }

    @PostMapping(value="/admin/registration/admin",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String,Object>  registerAdmin(@Valid @RequestBody Admin adminUser)
    {
        HashMap<String,Object> map = new HashMap<>();

        Admin admin = adminRepo.findByuserName(adminUser.getUserName());
        Lecturer lec = lecRepo.findByuserName(adminUser.getUserName());

        //check whether user is already exists
        if(admin!=null || lec!=null) {
            map.put("msg","user Name is already exists");
            return map;
        }
        adminUser.setRole("ADMIN");
        String pass = studentService.passGenerate();
        adminUser.setPassword(passwordEncoder.encode(pass));
        adminRepo.save(adminUser);

        String mail = adminUser.getEmail();
        String password ="Password: " + pass;
        String name = "User Name: " + adminUser.getUserName();
        String body = name+ "      " + password;
        //sendmail
        mailService.sendMail(mail,body  ,"Account Creation");


        //successfully registered and return the registered user
        map.put("Admin",adminRepo.findByuserName(adminUser.getUserName()));
        return map;
    }

    @PostMapping(value="/admin/registration/lecturer",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String,Object> registerLecturer(@Valid @RequestBody Lecturer lecturer)
    {
        HashMap<String,Object> map = new HashMap<>();
        Admin admin = adminRepo.findByuserName(lecturer.getUserName());
        Lecturer lec = lecRepo.findByuserName(lecturer.getUserName());
        //check whether user is already exists
        if(admin!=null || lec!=null) {
            map.put("msg","user Name is already exists");
            return map;
        }
        lecturer.setRole("LECTURER");
        String pass = studentService.passGenerate();
        lecturer.setPassword(passwordEncoder.encode(pass));

        lecturer.setPassword(passwordEncoder.encode(lecturer.getPassword()));
        lecRepo.save(lecturer);

        String mail = lecturer.getEmail();
        String password ="Password: " + pass;
        String name = "User Name: " + lecturer.getUserName();
        String body = name+ "      " + password;
        //sendmail
        mailService.sendMail(mail,body  ,"Account Creation");

        //successfully registered and return the registered user
        map.put("Lecturer",lecRepo.findByuserName(lecturer.getUserName()));
        return map;
    }

    @PostMapping(value="admin/student/delete")
    public HashMap<String, Object> deleteStudent(@PathVariable("student_id") String id){
        HashMap<String,Object> map = new HashMap<>();
        map = studentService.deleteStudent(id);
        return map;

    }

   @PostMapping(value="admin/student/update")
   public HashMap<String, Object> updateStudent(@RequestBody StudentPayload stu){
      HashMap<String,Object> map ;
      map = studentService.updateStudent(stu);
      return map;
   }

   @GetMapping(value = "admin/all/students")
    public List<Student> findAll(){
        return stuRepo.findAll();
   }



}
