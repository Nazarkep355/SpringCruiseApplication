package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.Staff;
import com.example.SpringCruiseApplication.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> findStaffPageable(int page) {
        return staffRepository.findAll(PageRequest.of(page - 1, 5));
    }

    public Integer count() {
        return staffRepository.countAll();
    }

    public Staff insert(Staff staff) {
        return staffRepository.save(staff);
    }

    public Staff createNewStaff(String name, String position) {
        Staff staff = new Staff();
        if (staffRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("error.staffWithNameExist");
        }
        staff.setName(name);
        staff.setPosition(position);
        staff.setEnable(true);
        return insert(staff);
    }
}
