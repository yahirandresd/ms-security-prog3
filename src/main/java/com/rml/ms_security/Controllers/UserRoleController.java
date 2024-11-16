package com.rml.ms_security.Controllers;

import com.rml.ms_security.Models.Permission;
import com.rml.ms_security.Models.Role;
import com.rml.ms_security.Models.User;
import com.rml.ms_security.Models.UserRole;
import com.rml.ms_security.Repositories.RoleRepository;
import com.rml.ms_security.Repositories.UserRepository;
import com.rml.ms_security.Repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user-role")
public class UserRoleController {
    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private RoleRepository theRoleRepository;

    @Autowired
    private UserRoleRepository theUserRoleRepository;

    @GetMapping("")
    public List<UserRole> find(){
        return this.theUserRoleRepository.findAll();
    }

    @PostMapping("user/{userId}/role/{roleId}")
    public UserRole create(@PathVariable String userId,
                           @PathVariable String roleId){
        User theUser=this.theUserRepository.findById(userId).orElse(null);
        Role theRole=this.theRoleRepository.findById(roleId).orElse(null);
        if(theUser!=null && theRole!=null){
            UserRole newUserRole=new UserRole();
            newUserRole.setUser(theUser);
            newUserRole.setRole(theRole);
            return this.theUserRoleRepository.save(newUserRole);
        }else{
            return null;
        }


    }
    @GetMapping("user/{userId}")
    public List<UserRole> getRolesByUser(@PathVariable String userId){
        return this.theUserRoleRepository.getRolesByUser(userId);
    }

    @GetMapping("role/{roleId}")
    public List<UserRole> getUsersByRole(@PathVariable String roleId){
        return this.theUserRoleRepository.getUsersByRole(roleId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        UserRole theUserRole=this.theUserRoleRepository.findById(id).orElse(null);
        if (theUserRole!=null){
          System.out.println("Si llega");
            this.theUserRoleRepository.delete(theUserRole);
        }
      System.out.println("No llega");
    }


}
