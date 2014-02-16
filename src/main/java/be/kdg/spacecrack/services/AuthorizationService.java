package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.controllers.ContactController;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ITokenRepository;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("tokenService")
public class AuthorizationService implements IAuthorizationService {

    @Autowired
    ITokenRepository tokenRepository;
    @Autowired
    IUserRepository userRepository;
    static Logger logger = LoggerFactory.getLogger(TokenController.class);
    @Autowired
    private ITokenStringGenerator tokenStringGenerator;

    public AuthorizationService() {
    }

    public AuthorizationService(ITokenRepository tokenRepository, IUserRepository userRepository, ITokenStringGenerator tokenStringGenerator){
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenStringGenerator = tokenStringGenerator;
    }

    @Override
    public AccessToken getAccessTokenByValue(String accessTokenValue) throws Exception {
        return tokenRepository.getAccessTokenByValue(accessTokenValue);
    }


    @Override
    public void createTestUser() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query query = session.createQuery("from User u where u.username = :testusername");
                query.setParameter("testusername", "test");
                if (query.list().size() < 1) {
                    session.saveOrUpdate(new User("test", "test"));
                }
                tx.commit();

            } catch (Exception e) {
                tx.rollback();
            }
       }finally {
           HibernateUtil.close(session);
       }
    }

    @Override
    public AccessToken login(User user) {
        User dbUser;

            dbUser = userRepository.getUser(user);


        if (dbUser == null) {
            throw new SpaceCrackUnauthorizedException();
        }
        AccessToken accessToken = dbUser.getToken();

        if (accessToken == null) {
            String tokenvalue = tokenStringGenerator.generateTokenString();
            accessToken = new AccessToken(tokenvalue);
            dbUser.setToken(accessToken);

            tokenRepository.saveAccessToken(dbUser, accessToken);
        }
        return accessToken;
    }


    @Override
    public void logout(String accessTokenValue) {
        AccessToken accessToken = tokenRepository.getAccessTokenByValue(accessTokenValue);

        try {
           userRepository.DeleteAccessToken(accessToken);
        } catch (Exception ex) {
            throw new SpaceCrackUnexpectedException("Unexpected exception happened while logging out");
        }
    }

    @Override
    public User getUserByAccessToken(String accessTokenValue, ContactController contactController) throws Exception {
        return userRepository.getUserByAccessToken(getAccessTokenByValue(accessTokenValue));
    }
}
