package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetDTO {
    private long id;
    private String name;
    private String status;

    /*
    Constructors
     */
    public PetDTO() {
    }

    public PetDTO(long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    /*
    Getters
     */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    /*
    Setters
     */
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
