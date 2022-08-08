package com.example.filedemo.model;

import javax.persistence.*;

@Entity
@Table(name = "encrypt_files")
public class EncryptFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  
    private Long id;

    private String name;

    private String type;

    @Lob
    private byte[] filecontent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(byte[] filecontent) {
		this.filecontent = filecontent;
	}

	public EncryptFiles(String name, String type, byte[] filecontent) {
		super();
		this.name = name;
		this.type = type;
		this.filecontent = filecontent;
		// TODO Auto-generated constructor stub
	}

	public EncryptFiles(Long id, String name, String type, byte[] filecontent) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.filecontent = filecontent;
	}

	public EncryptFiles() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

   
 }
