package dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDTO {
    private long id;
    private CategoryDTO category;
    private String name;
    private String status;
    private List<String> photoUrls;
    private List<TagDTO> tags;
}
