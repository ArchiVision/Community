package com.archivision.community.repo;

import com.archivision.community.document.City;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {
}
