package project4.Mongo.Interface;

import org.springframework.data.mongodb.repository.MongoRepository;

import project4.login.model.lpsolver.LpInput;
import project4.login.model.lpsolver.LpOutput;

public interface LpOutputRepository extends MongoRepository<LpOutput, String> {

	public LpOutput findById(String id);
}



