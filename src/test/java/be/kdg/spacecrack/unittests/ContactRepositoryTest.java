package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.repositories.ContactRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class ContactRepositoryTest {
    @Test
    public void testAddContact() throws Exception {
        Calendar calendar = new GregorianCalendar(2014,2,12);
        Contact contact = new Contact("firstname","lastname","email", calendar.getTime(),"image");
        ContactRepository contactRepository = new ContactRepository();
        contactRepository.addContact(contact);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        @SuppressWarnings("JpaQlInspection")Query q = session.createQuery("FROM Contact c WHERE c = :contact");
        q.setParameter("contact", contact);

        Contact actual = (Contact) q.uniqueResult();
        tx.commit();
        assertEquals("Should be in the db",contact.getContactId(), actual.getContactId());
    }
}
