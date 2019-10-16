package api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
    public static final SuccessResponse SUCCESS = new SuccessResponse(200, "Success");

    private int status;
    private String message;
}
