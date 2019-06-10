package com.smartdevicelink.test.rpc.enums;

import com.smartdevicelink.proxy.rpc.enums.Language;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a unit test class for the SmartDeviceLink library project class : 
 * {@link com.smartdevicelink.proxy.rpc.enums.Language}
 */
public class LanguageTests extends TestCase {

	/**
	 * Verifies that the enum values are not null upon valid assignment.
	 */
	public void testValidEnums () {	
		String example = "EN-US";
		Language enumEnUs = Language.valueForString(example);
		example = "EN-SA";
		Language enumEnSa = Language.valueForString(example);
		example = "HE-IL";
		Language enumHeIl = Language.valueForString(example);
		example = "RO-RO";
		Language enumRoRo = Language.valueForString(example);
		example = "UK-UA";
		Language enumUkUa = Language.valueForString(example);
		example = "ID-ID";
		Language enumIdId = Language.valueForString(example);
		example = "VI-VN";
		Language enumViVn = Language.valueForString(example);
		example = "MS-MY";
		Language enumMsMy = Language.valueForString(example);
		example = "HI-IN";
		Language enumHiIn = Language.valueForString(example);
		example = "NL-BE";
		Language enumNlBe = Language.valueForString(example);
		example = "EL-GR";
		Language enumElGr = Language.valueForString(example);
		example = "HU-HU";
		Language enumHuHu = Language.valueForString(example);
		example = "FI-FI";
		Language enumFiFi = Language.valueForString(example);
		example = "SK-SK";
		Language enumSkSk = Language.valueForString(example);
		example = "ES-MX";
		Language enumEsMx = Language.valueForString(example);
		example = "FR-CA";
		Language enumFrCa = Language.valueForString(example);
		example = "DE-DE";
		Language enumDeDe = Language.valueForString(example);
		example = "ES-ES";
		Language enumEsEs = Language.valueForString(example);
		example = "EN-GB";
		Language enumEnGb = Language.valueForString(example);
		example = "RU-RU";
		Language enumRuRu = Language.valueForString(example);
		example = "TR-TR";
		Language enumTrTr = Language.valueForString(example);
		example = "PL-PL";
		Language enumPlPl = Language.valueForString(example);
		example = "FR-FR";
		Language enumFrFr = Language.valueForString(example);
		example = "IT-IT";
		Language enumItIt = Language.valueForString(example);
		example = "SV-SE";
		Language enumSvSe = Language.valueForString(example);
		example = "PT-PT";
		Language enumPtPt = Language.valueForString(example);
		example = "NL-NL";
		Language enumNlNl = Language.valueForString(example);
		example = "EN-AU";
		Language enumEnAu = Language.valueForString(example);
		example = "ZH-CN";
		Language enumZhCn = Language.valueForString(example);
		example = "ZH-TW";
		Language enumZhTw = Language.valueForString(example);
		example = "JA-JP";
		Language enumJaJp = Language.valueForString(example);
		example = "AR-SA";
		Language enumArSa = Language.valueForString(example);
		example = "KO-KR";
		Language enumKoKr = Language.valueForString(example);
		example = "PT-BR";
		Language enumPtBr = Language.valueForString(example);
		example = "CS-CZ";
		Language enumCsCz = Language.valueForString(example);
		example = "DA-DK";
		Language enumDaDk = Language.valueForString(example);
		example = "NO-NO";
		Language enumNoNo = Language.valueForString(example);
		example = "EN-IN";
		Language enumEnIn = Language.valueForString(example);
		example = "TH-TH";
		Language enumThTh = Language.valueForString(example);
		
		assertNotNull("EN-US returned null", enumEnUs);
		assertNotNull("EN-SA returned null", enumEnSa);
		assertNotNull("HE-IL returned null", enumHeIl);
		assertNotNull("RO-RO returned null", enumRoRo);
		assertNotNull("UK-UA returned null", enumUkUa);
		assertNotNull("ID-ID returned null", enumIdId);
		assertNotNull("VI-VN returned null", enumViVn);
		assertNotNull("MS-MY returned null", enumMsMy);
		assertNotNull("HI-IN returned null", enumHiIn);
		assertNotNull("NL-BE returned null", enumNlBe);
		assertNotNull("EL-GR returned null", enumElGr);
		assertNotNull("HU-HU returned null", enumHuHu);
		assertNotNull("FI-FI returned null", enumFiFi);
		assertNotNull("SK-SK returned null", enumSkSk);
		assertNotNull("ES-MX returned null", enumEsMx);
		assertNotNull("FR-CA returned null", enumFrCa);
		assertNotNull("DE-DE returned null", enumDeDe);
		assertNotNull("ES-ES returned null", enumEsEs);
		assertNotNull("EN-GB returned null", enumEnGb);
		assertNotNull("RU-RU returned null", enumRuRu);
		assertNotNull("TR-TR returned null", enumTrTr);
		assertNotNull("PL-PL returned null", enumPlPl);
		assertNotNull("FR-FR returned null", enumFrFr);
		assertNotNull("IT-IT returned null", enumItIt);
		assertNotNull("SV-SE returned null", enumSvSe);
		assertNotNull("PT-PT returned null", enumPtPt);
		assertNotNull("NL-NL returned null", enumNlNl);
		assertNotNull("EN-AU returned null", enumEnAu);
		assertNotNull("ZH-CN returned null", enumZhCn);
		assertNotNull("ZH-TW returned null", enumZhTw);
		assertNotNull("JA-JP returned null", enumJaJp);
		assertNotNull("AR-SA returned null", enumArSa);
		assertNotNull("KO-KR returned null", enumKoKr);
		assertNotNull("PT-BR returned null", enumPtBr);
		assertNotNull("CS-CZ returned null", enumCsCz);
		assertNotNull("DA-DK returned null", enumDaDk);
		assertNotNull("NO-NO returned null", enumNoNo);
		assertNotNull("EN-IN returned null", enumEnIn);
		assertNotNull("TH-TH returned null", enumThTh);
	}

	/**
	 * Verifies that an invalid assignment is null.
	 */
	public void testInvalidEnum () {
		String example = "eN-Us";
		try {
		    Language temp = Language.valueForString(example);
            assertNull("Result of valueForString should be null.", temp);
		}
		catch (IllegalArgumentException exception) {
            fail("Invalid enum throws IllegalArgumentException.");
		}
	}

	/**
	 * Verifies that a null assignment is invalid.
	 */
	public void testNullEnum () {
		String example = null;
		try {
		    Language temp = Language.valueForString(example);
            assertNull("Result of valueForString should be null.", temp);
		}
		catch (NullPointerException exception) {
            fail("Null string throws NullPointerException.");
		}
	}

	/**
	 * Verifies the possible enum values of Language.
	 */
	public void testListEnum() {
 		List<Language> enumValueList = Arrays.asList(Language.values());

		List<Language> enumTestList = new ArrayList<Language>();
		
		enumTestList.add(Language.EN_US);
		enumTestList.add(Language.EN_SA);
		enumTestList.add(Language.HE_IL);
		enumTestList.add(Language.RO_RO);
		enumTestList.add(Language.UK_UA);
		enumTestList.add(Language.ID_ID);
		enumTestList.add(Language.VI_VN);
		enumTestList.add(Language.MS_MY);
		enumTestList.add(Language.HI_IN);
		enumTestList.add(Language.NL_BE);
		enumTestList.add(Language.EL_GR);
		enumTestList.add(Language.HU_HU);
		enumTestList.add(Language.FI_FI);
		enumTestList.add(Language.SK_SK);
		enumTestList.add(Language.ES_MX);
		enumTestList.add(Language.FR_CA);
		enumTestList.add(Language.DE_DE);
		enumTestList.add(Language.ES_ES);
		enumTestList.add(Language.EN_GB);
		enumTestList.add(Language.RU_RU);
		enumTestList.add(Language.TR_TR);
		enumTestList.add(Language.PL_PL);
		enumTestList.add(Language.FR_FR);
		enumTestList.add(Language.IT_IT);
		enumTestList.add(Language.SV_SE);
		enumTestList.add(Language.PT_PT);
		enumTestList.add(Language.NL_NL);
		enumTestList.add(Language.EN_AU);
		enumTestList.add(Language.ZH_CN);
		enumTestList.add(Language.ZH_TW);
		enumTestList.add(Language.JA_JP);
		enumTestList.add(Language.AR_SA);
		enumTestList.add(Language.KO_KR);
		enumTestList.add(Language.PT_BR);
		enumTestList.add(Language.CS_CZ);
		enumTestList.add(Language.DA_DK);
		enumTestList.add(Language.NO_NO);
		enumTestList.add(Language.EN_IN);
		enumTestList.add(Language.TH_TH);
		
		assertTrue("Enum value list does not match enum class list", 
				enumValueList.containsAll(enumTestList) && enumTestList.containsAll(enumValueList));
	}	
}