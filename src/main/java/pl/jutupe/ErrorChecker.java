package pl.jutupe;

import io.restassured.path.json.JsonPath;
import pl.jutupe.enums.ErrorType;
import pl.jutupe.object.Errors;

import java.util.List;

class ErrorChecker {
    private List<Errors> errors;

    ErrorChecker(JsonPath jsonPath) {
        this.errors = jsonPath.getList("errors", Errors.class);
    }

    boolean checkForError(ErrorType type){
        for (Errors error : errors) {
            if (error.getCode() == type.getId()){
                return true;
            }
        }
        return false;
    }
}
