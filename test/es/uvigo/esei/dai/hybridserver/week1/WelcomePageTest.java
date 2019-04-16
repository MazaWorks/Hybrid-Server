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

import static es.uvigo.esei.dai.hybridserver.utils.TestUtils.getContent;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import es.uvigo.esei.dai.hybridserver.utils.HybridServerTestCase;

public class WelcomePageTest extends HybridServerTestCase {
	@Test
	public void testWelcome() throws IOException {
		assertThat(getContent(url), containsString("Hybrid Server"));
	}
	
	@Test
	public void testMultipleWelcome() throws IOException {
		for (int i = 0; i < 10; i++) {
			assertThat(getContent(url), containsString("Hybrid Server"));
		}
	}
}
