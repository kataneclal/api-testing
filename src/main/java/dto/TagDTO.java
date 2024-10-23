package dto;

public class TagDTO {
    private long id;
    private String name;

    /*
    Constructors
     */
    public TagDTO() {
    }

    public TagDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /*
    Getters & setters
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
