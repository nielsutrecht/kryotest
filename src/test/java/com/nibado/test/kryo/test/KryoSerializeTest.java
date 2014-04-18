package com.nibado.test.kryo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.nibado.test.kryo.Project;
import com.nibado.test.kryo.UUID;

import static org.junit.Assert.*;

public class KryoSerializeTest {
	
	private static final File TEST_FILE = new File("projects.kryo"); 
	private final Kryo kryo = new Kryo();
	public KryoSerializeTest() {
		Serializer<UUID> serializer = new Serializer<UUID>() {
			
			@Override
			public void write(Kryo kryo, Output output, UUID id) {
				output.writeLong(id.getMostSigBits());
				output.writeLong(id.getLeastSigBits());
			}
			
			@Override
			public UUID read(Kryo kryo, Input input, Class<UUID> clazz) {
				long mostSigBits = input.readLong();
				long leastSigBits = input.readLong();
				
				return new UUID(mostSigBits, leastSigBits);
			}
		};
		kryo.register(UUID.class, serializer);
	}
	
	@Before
	public void setup() {
		TEST_FILE.delete();
	}
	
	@After
	public void tearDown() {
		//TEST_FILE.delete();
	}
	
	@Test
	public void testSerialize() {
		save();
	}
	
	@Test
	public void testDeserialize() {
		save();
		
		Input input;
		Project[] projects = null;
		try {
			input = new Input(new FileInputStream(TEST_FILE));
			projects = kryo.readObject(input, Project[].class);
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
		
		assertNotNull(projects);

		for(int i = 0;i < projects.length;i++) {
			assertEquals("Project " + i, projects[i].getName());
			assertEquals("Project description " + i, projects[i].getDescription());
			assertEquals(i * 10, projects[i].getId().getLeastSigBits());
			assertEquals(i * 100, projects[i].getId().getMostSigBits());
		}
		
	}
	
	private void save() {
		Project[] projects = new Project[3];
		for(int i = 0;i < projects.length;i++) {
			projects[i] = new Project();
			projects[i].setName("Project " + i);
			projects[i].setDescription("Project description " + i);
			UUID id = new UUID(i * 100, i * 10);
			projects[i].setId(id);
		}
		
		Output output;
		try {
			output = new Output(new FileOutputStream(TEST_FILE));
			kryo.writeObject(output, projects);
			output.close();
		} 
		catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
		
	}
}
