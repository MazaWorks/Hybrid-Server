/**
 *  HybridServer
 *  Copyright (C) 2018 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.week1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	HTTPBadRequestsTest.class,
	HTTPRequestGETRootTest.class,
	HTTPRequestGETResourceTest.class,
	HTTPRequestGETResourcesTest.class,
	HTTPRequestGETParametersTest.class,
	HTTPRequestPOSTOneParameterTest.class,
	HTTPRequestPOSTMultipleParametersTest.class,
	HTTPRequestPOSTEncodedTest.class,
	HTTPResponseNoContentTest.class,
	HTTPResponseNoContentWithHeadersTest.class,
	HTTPResponseTest.class,
	HTTPResponseWithHeadersTest.class
})
public class HTTPRequestResponseSuite {

}
