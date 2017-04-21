
package edu.ilstu.it;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.ilstu.it package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendSMS_QNAME = new QName("http://it.ilstu.edu/", "sendSMS");
    private final static QName _SendSMSResponse_QNAME = new QName("http://it.ilstu.edu/", "sendSMSResponse");
    private final static QName _GetCarriers_QNAME = new QName("http://it.ilstu.edu/", "getCarriers");
    private final static QName _GetCarriersResponse_QNAME = new QName("http://it.ilstu.edu/", "getCarriersResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.ilstu.it
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendSMS }
     * 
     */
    public SendSMS createSendSMS() {
        return new SendSMS();
    }

    /**
     * Create an instance of {@link SendSMSResponse }
     * 
     */
    public SendSMSResponse createSendSMSResponse() {
        return new SendSMSResponse();
    }

    /**
     * Create an instance of {@link GetCarriers }
     * 
     */
    public GetCarriers createGetCarriers() {
        return new GetCarriers();
    }

    /**
     * Create an instance of {@link GetCarriersResponse }
     * 
     */
    public GetCarriersResponse createGetCarriersResponse() {
        return new GetCarriersResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSMS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it.ilstu.edu/", name = "sendSMS")
    public JAXBElement<SendSMS> createSendSMS(SendSMS value) {
        return new JAXBElement<SendSMS>(_SendSMS_QNAME, SendSMS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSMSResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it.ilstu.edu/", name = "sendSMSResponse")
    public JAXBElement<SendSMSResponse> createSendSMSResponse(SendSMSResponse value) {
        return new JAXBElement<SendSMSResponse>(_SendSMSResponse_QNAME, SendSMSResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCarriers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it.ilstu.edu/", name = "getCarriers")
    public JAXBElement<GetCarriers> createGetCarriers(GetCarriers value) {
        return new JAXBElement<GetCarriers>(_GetCarriers_QNAME, GetCarriers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCarriersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it.ilstu.edu/", name = "getCarriersResponse")
    public JAXBElement<GetCarriersResponse> createGetCarriersResponse(GetCarriersResponse value) {
        return new JAXBElement<GetCarriersResponse>(_GetCarriersResponse_QNAME, GetCarriersResponse.class, null, value);
    }

}
