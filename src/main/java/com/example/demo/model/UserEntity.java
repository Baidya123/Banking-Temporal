/*
 * package com.example.demo.model;
 * 
 * import javax.persistence.*; import java.util.List;
 * 
 * @Entity
 * 
 * @Table(name = "User") public class UserEntity {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 * 
 * @Column(name = "firstName") private String firstName;
 * 
 * @Column(name = "lastName") private String lastName;
 * 
 * @Column(name = "email") private String email;
 * 
 * @Column(name = "account_number") private String accountNumber;
 * 
 * @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade =
 * CascadeType.ALL) private List<Account> accounts;
 * 
 * public Long getId() { return id; }
 * 
 * public void setId(Long id) { this.id = id; }
 * 
 * public String getFirstName() { return firstName; }
 * 
 * public void setFirstName(String firstName) { this.firstName = firstName; }
 * 
 * public String getLastName() { return lastName; }
 * 
 * public void setLastName(String lastName) { this.lastName = lastName; }
 * 
 * public String getEmail() { return email; }
 * 
 * public void setEmail(String email) { this.email = email; }
 * 
 * public String getAccountNumber() { return accountNumber; }
 * 
 * public void setAccountNumber(String accountNumber) { this.accountNumber =
 * accountNumber; }
 * 
 * public List<Account> getAccounts() { return accounts; }
 * 
 * public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
 * 
 * }
 */