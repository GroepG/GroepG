package be.kdg.spacecrack.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class SpaceCrackNotAcceptableException extends RuntimeException {
    public SpaceCrackNotAcceptableException(String message) {
        super(message);
    }
}
