package Group10.example.API.Model;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.HashSet;

@Document(collection = "Groups")
public class Group {
    @Override
    public String toString() {
        return "Group{" +
                "groupID='" + groupID + '\'' +
                ", groupName='" + groupName + '\'' +
                ", subject='" + subject + '\'' +
                ", studentList=" + studentList +
                ", lecList=" + lecList +
                ", courseList=" + courseList +
                '}';
    }

    public Group(@NotNull(message = "Group Name is mandatory") String groupName, String subject, HashSet<String> studentList, HashSet<String> lecList, HashSet<String> courseList) {
        this.groupName = groupName;
        this.subject = subject;
        this.studentList = studentList;
        this.lecList = lecList;
        this.courseList = courseList;
    }

    @Id
    private String groupID;

    @NotNull(message = "Group Name is mandatory")
    private String groupName;

    private String subject;


    public HashSet<String> studentList = new HashSet<>();

    public HashSet<String>  lecList =  new HashSet<>();

    public HashSet<String> courseList = new HashSet<>();

    public HashSet<String> getCourseList() {
        return courseList;
    }

    public void setCourseList(HashSet<String> courseList) {
        this.courseList = courseList;
    }

    public  HashSet<String> getLecList() {
        return lecList;
    }

    public void setLecList( HashSet<String> lecList) {
        this.lecList = lecList;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashSet<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(HashSet<String>  studentList) {
        this.studentList = studentList;
    }
}
