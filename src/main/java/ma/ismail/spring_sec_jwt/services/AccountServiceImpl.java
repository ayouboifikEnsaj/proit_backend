package ma.ismail.spring_sec_jwt.services;

import jakarta.transaction.Transactional;
import ma.ismail.spring_sec_jwt.entites.Aeroport;
import ma.ismail.spring_sec_jwt.entites.AppRole;
import ma.ismail.spring_sec_jwt.entites.AppUser;
import ma.ismail.spring_sec_jwt.repository.AeroportRespository;
import ma.ismail.spring_sec_jwt.repository.AppRoleRepository;
import ma.ismail.spring_sec_jwt.repository.AppUserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppRoleRepository appRoleRepository;
    private AeroportRespository aeroportRespository;
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private AeroportService aeroportService;

    public AccountServiceImpl(AppRoleRepository appRoleRepository, AeroportRespository aeroportRespository, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, AeroportService aeroportService) {
        this.appRoleRepository = appRoleRepository;
        this.aeroportRespository = aeroportRespository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.aeroportService = aeroportService;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String pw=appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        System.out.println("Roles"+appUser.getRoles());
        appUserRepository.save(appUser);

        return appUser;
    }

    @Override
    public AppUser updateUser(AppUser appUser) {
        System.out.println("---------- AppUser"+appUser);
        if(appUser!=null){
                if(appUser.getPassword().equals("")){
                    String password=appUserRepository.
                            findAppUserById(appUser.getId()).getPassword();
                    appUser.setPassword(password);
                    appUserRepository.save(appUser);
                }
                else {
                    String pw=appUser.getPassword();
                    appUser.setPassword(passwordEncoder.encode(pw));
                    appUserRepository.save(appUser);
                    return appUser;
                }
        }
        return null;
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {

        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUsername(String username, List<AppRole> roles) {
        //Cette Methode pour le Test
        System.out.println("add users to roles 1");
        AppUser appUser=appUserRepository.findByUsername(username);
        System.out.println("bien add users to roles 1"+username);
        for (AppRole role:roles
             ) {
            System.out.println("add users to roles"+roles);
            AppRole appRole=appRoleRepository.findByRoleName(role.getRoleName());
            appUser.getRoles().add(appRole);
            System.out.println("OUII");
        }

    }

    @Override
    public AppUser loadUserByUsername(String username) {

        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public List<AppUser> getUsersByRole(String role) {
        return appUserRepository.findByRoles_RoleName(role);
    }
    @Override
    public boolean verifyCredentials(String username, String password) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser != null && passwordEncoder.matches(password, appUser.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public List<AppUser> findByRoles_RoleNameAndAeroport1_AeroportName(String role, String aeroportName) {
        //AppUser user=appUserRepository.findByUsername(username);
        //return appUserRepository.findByRoles_RoleNameAndAeroport1_AeroportName(role,user.getAeroport1().getAeroportName());
        //Aeroport aeroport=aeroportRespository.findAeroportByAeroportName(aeroportName);
        return appUserRepository.findByRoles_RoleNameAndAeroport_AeroportName(role,aeroportName);
    }

    @Override
    public void importAppUserData(MultipartFile file) throws IOException {
        System.out.println("Start service");


        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(1);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }

            AppUser appUser = new AppUser();
            System.out.println("user avant"+appUser);
            try {
                row.getCell(1).getStringCellValue().trim();
            }catch (Exception e){
                workbook.close();
                return;
            }
            System.out.println("le premire ligne"+row.getCell(1).getStringCellValue().trim());
            appUser.setFirstname(row.getCell(1).getStringCellValue().trim());
            appUser.setLastname(row.getCell(2).getStringCellValue().trim());
            appUser.setUsername(row.getCell(3).getStringCellValue().trim());
            System.out.println("user avant encoding password"+appUser);
            System.out.println("user avant encoding password---");
            System.out.println("user avant encoding airoport"+row.getCell(5).getStringCellValue().trim());
            System.out.println("user avant encoding role"+row.getCell(6).getStringCellValue().trim());
            System.out.println("user avant encoding password"+row.getCell(4).getStringCellValue().trim());
            appUser.setPassword(passwordEncoder.encode(row.getCell(4).getStringCellValue().trim()));
            System.out.println("user befor role adding"+appUser);
            AppRole role=appRoleRepository.findByRoleName(row.getCell(6).getStringCellValue().trim());
            List<AppRole> roles=new ArrayList<>();
            roles.add(role);
            appUser.setRoles(roles);
            System.out.println("user after role adding"+appUser);
            String aeroportName = row.getCell(5).getStringCellValue().trim();
            System.out.println("AeroportName : "+aeroportName);
            Aeroport aeroport = aeroportRespository.findAeroportByAeroportName(aeroportName);
            if (aeroport != null) {
                System.out.println("BIen Recu");
                appUser.setAeroport(aeroport);
                System.out.println("Apres BIen Recu");

            } else {
                System.out.println("Aeroport non trouvé pour le nom : " + aeroportName);
            }
            appUserRepository.save(appUser);
            System.out.println("bien  save");
        }
        workbook.close();
    }



}
