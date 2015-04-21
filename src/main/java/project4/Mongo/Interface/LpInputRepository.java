package project4.Mongo.Interface;

import org.springframework.data.mongodb.repository.MongoRepository;
import project4.login.model.lpsolver.LpInput;

public interface LpInputRepository extends MongoRepository<LpInput, String> {

	public LpInput findById(String id);
}



