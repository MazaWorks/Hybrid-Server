/**
 *  HybridServer
 *  Copyright (C) 2017 Miguel Reboiro-Jato
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
package es.uvigo.esei.dai.hybridserver;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.uvigo.esei.dai.hybridserver.week9.ConvertedXMLTest;
import es.uvigo.esei.dai.hybridserver.week9.InvalidConvertedXMLTest;
import es.uvigo.esei.dai.hybridserver.week9.InvalidXMLConfigurationLoaderTest;
import es.uvigo.esei.dai.hybridserver.week9.XMLConfigurationLoaderTest;

@RunWith(Suite.class)
@SuiteClasses({
	ConvertedXMLTest.class,
	InvalidConvertedXMLTest.class,
	InvalidXMLConfigurationLoaderTest.class,
	XMLConfigurationLoaderTest.class
})
public class Week9TestSuite {

}
