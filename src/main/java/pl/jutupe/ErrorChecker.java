package pl.jutupe;

import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import pl.jutupe.enums.ErrorType;
import pl.jutupe.object.Errors;
import java.util.List;

class ErrorChecker {
    private List<Errors> errors;

    ErrorChecker(JsonPath jsonPath) {
        try {
            this.errors = jsonPath.getList("errors", Errors.class);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(Constants.ERROR_CHECKER_BAD_JSONPATH);
        }
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