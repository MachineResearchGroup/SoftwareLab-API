package com.swl.service;

import com.swl.exceptions.business.InvalidImageException;
import com.swl.models.management.Organization;
import com.swl.models.management.Team;
import com.swl.models.people.User;
import com.swl.models.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UploadService {

    @Autowired
    private final OrganizationService organizationService;

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final TeamService teamService;

    @Autowired
    private final UserService userService;


    public void uploadImageOfUser(MultipartFile imageFile, Integer idUser) {
        try {
            byte[] imageArr = imageFile.getBytes();

            User user = userService.getUser(idUser);
            user.setProfileImage(imageArr);
            userService.saveUser(user);

        } catch (IOException e) {
            throw new InvalidImageException(e.getMessage());
        }
    }


    public void uploadImageOfOrganization(MultipartFile imageFile, Integer idOrganization) {
        try {
            byte[] imageArr = imageFile.getBytes();

            Organization organization = organizationService.getOrganization(idOrganization);
            organization.setIcon(imageArr);
            organizationService.saveOrganization(organization);

        } catch (IOException e) {
            throw new InvalidImageException(e.getMessage());
        }
    }


    public void uploadImageOfProject(MultipartFile imageFile, Integer idProject) {
        try {
            byte[] imageArr = imageFile.getBytes();

            Project project = projectService.getProject(idProject);
            project.setIcon(imageArr);
            projectService.saveProject(project);

        } catch (IOException e) {
            throw new InvalidImageException(e.getMessage());
        }
    }


    public void uploadImageOfTeam(MultipartFile imageFile, Integer idTeam) {
        try {
            byte[] imageArr = imageFile.getBytes();

            Team team = teamService.getTeam(idTeam);
            team.setIcon(imageArr);
            teamService.saveTeam(team);

        } catch (IOException e) {
            throw new InvalidImageException(e.getMessage());
        }
    }

}
