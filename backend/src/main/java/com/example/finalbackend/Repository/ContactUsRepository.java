package com.example.finalbackend.Repository;

import com.example.finalbackend.Model.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs,Integer> {

}
