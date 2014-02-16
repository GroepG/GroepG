package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.controllers.ContactController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;

public interface IAuthorizationService {
    public AccessToken getAccessTokenByValue(String accessTokenValue) throws Exception;

    void createTestUser();

    AccessToken login(User user);

    void logout(String accessTokenValue);

    User getUserByAccessToken(String accessTokenValue, ContactController contactController) throws Exception;
}
