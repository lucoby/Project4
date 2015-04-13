package project4.Mongo.Interface;

import org.springframework.data.mongodb.repository.MongoRepository;

import project4.login.model.StudentModel;

public interface StudentRepository extends MongoRepository<StudentModel, String> {
    
    public StudentModel findById(String id);

}
