package centigrade.reported_accounts;

import centigrade.accounts.Account;

import javax.persistence.*;

@Entity
@Table(name="reportedusers")
public class ReportedAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    private long reportedId;

    @Transient
    private Account account;

    @Column(name="id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name="reportedid")
    public long getReportedId() {
        return reportedId;
    }

    public void setReportedId(long reportedId) {
        this.reportedId = reportedId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
