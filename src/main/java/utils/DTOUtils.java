package utils;

import dto.*;

public class DTOUtils {

    /*
     Сreate a PetDTO
     */
    public static PetDTO createPetDTO(long id, String name, String status) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(id);
        petDTO.setName(name);
        petDTO.setStatus(status);
        return petDTO;
    }

    /*
     Сreate a UserDTO
     */
    public static UserDTO createUserDTO(long id, String username, String firstName, String lastName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUsername(username);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        return userDTO;
    }

}
