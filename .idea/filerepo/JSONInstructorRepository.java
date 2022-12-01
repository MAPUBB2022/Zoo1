package repository.filerepo;
import repository.InstructorRepository;
import domain.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileInstructorRepository implements InstructorRepository {

    private String filepath;
    private List<Instructor> allInstructors;

    public FileInstructorRepository(String filepath) {
        this.filepath = filepath;
        this.allInstructors = new ArrayList<>();
        this.populateInstructors();
    }



    public void populateInstructors(){

        Instructor instructor1 = new Instructor("i1","James", "Parker", "123456");
        Instructor instructor2 = new Instructor("i2","James", "John", "qwerty");
        Instructor instructor3 = new Instructor("i3", "Lucy", "Misterious", "abc123");
        Instructor instructor4 = new Instructor("i4", "Katy", "Gal", "123321");
        Instructor instructor5 = new Instructor("i5","Camila", "Pop", "password1");
        Instructor instructor6 = new Instructor("i6","Mircea", "Miron", "abcd1234");

        this.allInstructors.add(instructor1);
        this.allInstructors.add(instructor2);
        this.allInstructors.add(instructor3);
        this.allInstructors.add(instructor4);
        this.allInstructors.add(instructor5);
        this.allInstructors.add(instructor6);

        File file = new File(this.filepath);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.allInstructors);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Instructor> getAllInstructors() {
//        List<Instructor> instructors  = new ArrayList<>();
//        File file = new File(this.filepath);
//        FileInputStream fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream(file);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        boolean x = true;
//        while(x) {
//            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
//                Instructor instructor = (Instructor) objectInputStream.readObject();
//                if (instructor != null) {
//                    instructors.add(instructor);
//                } else {
//                    x = false;
//                }
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return instructors;
        return allInstructors;
    }

    @Override
    public void delete(String id) {
        File file = new File(this.filepath);
        List<Instructor> instructors = new ArrayList<>();
        instructors = getAllInstructors();
        int idx;
        try {
            for(Instructor instructor: this.getAllInstructors()){
                if (instructor.getID().equals(id)){
                    idx = this.getAllInstructors().indexOf(instructor);
                    instructors.remove(idx);
                }
            }
            file.delete();   // file loeschen

            // die Daten ohne geloschtes Element zuruckschreiben
            File file2 = new File(this.filepath);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(instructors);
            objectOutputStream.close();
            fileOutputStream.close();
            this.allInstructors = instructors;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Instructor findByID(String id) {
        try {
            for (Instructor instructor : this.allInstructors) {
                if (instructor.getID().equals(id)) {
                    return instructor;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(Instructor instructor, String id) {
        boolean found = false;
        File file = new File(this.filepath);
        try{
            //String inputID = "{\"ID\": " + ID + " }";
            Instructor instructor1 = this.findByID(id);
            int position = this.allInstructors.indexOf(instructor1);
            this.allInstructors.set(position,instructor);

            file.delete();   // file loeschen

            File file2 = new File(this.filepath);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.allInstructors);
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Instructor instructor) {
        File file =  new File(this.filepath);
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(instructor));
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}




// JSON

//package repository.filerepo;
//
//import com.fasterxml.jackson.core.exc.StreamWriteException;
//import com.fasterxml.jackson.databind.*;
//import repository.InstructorRepository;
//import domain.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
//public class JSONInstructorRepository implements InstructorRepository {
//
//    private String filepath;
//
//    public JSONInstructorRepository(String filepath) {
//        this.filepath = filepath;
//        this.populateInstructors();
//    }
//
//    public void populateInstructors(){
//
//        Instructor instructor1 = new Instructor("JP1","James", "Parker", "12345");
//        Instructor instructor2 = new Instructor("JJ2", "James", "John", "6789");
//        Instructor instructor3 = new Instructor("ML", "Misterious", "Lucy", "X456");
//        Instructor instructor4 = new Instructor("GK4", "Gal", "Katy", "67H85");
//        Instructor instructor5 = new Instructor("PC5", "Pop", "Camila", "I890");
//
//        this.add(instructor1);
//        this.add(instructor2);
//        this.add(instructor3);
//        this.add(instructor4);
//        this.add(instructor5);
//
//        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            for(Instructor i: this.getAllInstructors()) {
//                mapper.writeValue(Paths.get(filepath).toFile(), i);
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Override
//    public List<Instructor> getAllInstructors() {
//        List<Instructor> instructors = new ArrayList<>();
//
//        try {    // create object mapper instance
//            ObjectMapper mapper = new ObjectMapper();
//            instructors = Arrays.asList(mapper.readValue(Paths.get(filepath).toFile(),Instructor[].class));
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return instructors;
//    }
//
//    @Override
//    public void delete(String id) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(new File(filepath));
//            Iterator<JsonNode> nodes = jsonNode.elements();
//            while (nodes.hasNext()) {
//                if (nodes.next().get("id").textValue().equals(id)) {
//                    nodes.remove();
//                }
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public Instructor findByID(String id) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            List<Instructor> instructors = Arrays.asList( mapper.readValue(new File(filepath), Instructor[].class) );
//
//            for (Instructor i: instructors) {
//                if(i.getID().equals(id)){
//                    return i;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }
//
//    @Override
//    public void update(Instructor instructor, String id) {
//        try{
//            String inputID = "{\"id\": " + id + " }";
//            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectReader objectReader = objectMapper.readerForUpdating(instructor);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void add(Instructor instructor) {
//        List<Instructor> instructorList = new ArrayList<>();
//        try{
//            instructorList = this.getAllInstructors();
//
//            // delete the entire file
//            File file = new File(filepath);
//            file.delete();
//
//            instructorList.add(instructor);
//
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(Paths.get(filepath).toFile(),instructorList);
//        } catch (StreamWriteException e) {
//            throw new RuntimeException(e);
//        } catch (DatabindException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
