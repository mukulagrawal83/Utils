package com.fnis.xes.framework.filter;

import mockit.Deencapsulation;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author morel
 */
@Test
public class BetterDebugFilterTest {

   private BetterDebugFilter debugFilter = new BetterDebugFilter();
   private Logger log = Logger.getLogger(BetterDebugFilterTest.class);

   private String ampersandTransactXMLOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRANSACT_XML_OUTPUT><APP_NUM>000224727</APP_NUM><XPRESS_REF>0HY11821</XPRESS_REF><ERROR_BLOCK/><SDS_RESULTS><DECISION>Refer</DECISION><REASON_CODE_ARRAY1>FR09</REASON_CODE_ARRAY1><REASON_CODE_ARRAY2>KR12</REASON_CODE_ARRAY2><REASON_CODE_TEXTS1>Minor PEPS &amp; SANC Required</REASON_CODE_TEXTS1><REASON_CODE_TEXTS2>SINGLE APPLICANT FAILED IDIQ</REASON_CODE_TEXTS2><AIP_VALID_TO_DATE>00000000</AIP_VALID_TO_DATE><CREDIT_LIMIT>00000</CREDIT_LIMIT><SHADOW_LIMIT>00000</SHADOW_LIMIT><MAX_TRANSFER_AMOUNT>00000</MAX_TRANSFER_AMOUNT><MAX_TRANSFER_PERCENT>000.00</MAX_TRANSFER_PERCENT><QUALIFIED_APR>000.00</QUALIFIED_APR><QUALIFIED_MONTHLY_INT>000.000000</QUALIFIED_MONTHLY_INT><TOTAL_COST_CREDIT>00000.00</TOTAL_COST_CREDIT><TOTAL_AMOUNT_PAYABLE>00000.00</TOTAL_AMOUNT_PAYABLE><PURCH_INTRO_PERIOD>00</PURCH_INTRO_PERIOD><PURCH_INTRO_RATE_MONTHLY>000.000</PURCH_INTRO_RATE_MONTHLY><PURCH_INTRO_RATE_SIMPLE_ANN>000.000</PURCH_INTRO_RATE_SIMPLE_ANN><PURCH_GOTO_RATE_MONTHLY>000.000</PURCH_GOTO_RATE_MONTHLY><PURCH_GOTO_RATE_SIMPLE_ANN>000.000</PURCH_GOTO_RATE_SIMPLE_ANN><PURCH_GOTO_RATE_AER>000.000</PURCH_GOTO_RATE_AER><BAL_TRANSER_INTRO_PERIOD>00</BAL_TRANSER_INTRO_PERIOD><BAL_TRANSER_INTRO_RATE_MONTHLY>000.000</BAL_TRANSER_INTRO_RATE_MONTHLY><BAL_TRANSER_INTRO_RATE_SIMPLE_ANN>000.000</BAL_TRANSER_INTRO_RATE_SIMPLE_ANN><BAL_TRANSER_GOTO_RATE_MONTHLY>000.000</BAL_TRANSER_GOTO_RATE_MONTHLY><BAL_TRANSER_GOTO_RATE_SIMPLE_ANN>000.000</BAL_TRANSER_GOTO_RATE_SIMPLE_ANN><BAL_TRANSER_GOTO_RATE_AER>000.000</BAL_TRANSER_GOTO_RATE_AER><INTRO_BT_CHARGE>00.00</INTRO_BT_CHARGE><CASH_CHARGE_PERCENT>00.00</CASH_CHARGE_PERCENT><CASH_CHARGE_MIN_AMOUNT>00.00</CASH_CHARGE_MIN_AMOUNT><LATE_PAYMENT_FEE>00.00</LATE_PAYMENT_FEE><GET_IDIQ_MA_FLAG>N</GET_IDIQ_MA_FLAG><GET_IDIQ_JA_FLAG>N</GET_IDIQ_JA_FLAG></SDS_RESULTS><SPARE/><NOTES/></TRANSACT_XML_OUTPUT>";
   private String invalidXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRANSACT_XML_OUTPUT>&</TRANSACT_XML_OUTPUT>";
   
   @BeforeClass
   public void setup() {
      BasicConfigurator.configure();
   }
   
   @Test
   public void ampersandTest() {
      String prettyPrint = Deencapsulation.invoke(debugFilter, "getPayloadContent", ampersandTransactXMLOutput);
      log.debug("Output: " + prettyPrint);
      
      Assert.assertNotNull(prettyPrint);
      Assert.assertNotSame(ampersandTransactXMLOutput, prettyPrint);
   }

   @Test
   public void expectNullOnInvalidXml() throws Exception {
      String prettyPrint = Deencapsulation.invoke(debugFilter, "getPayloadContent", invalidXML);
      log.debug("Output: " + prettyPrint);
      
      Assert.assertNull("Impossible to be not null???", prettyPrint);
   }
}
