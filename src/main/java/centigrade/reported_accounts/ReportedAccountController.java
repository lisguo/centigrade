package centigrade.reported_accounts;

import centigrade.accounts.Account;
import centigrade.accounts.AccountService;
import centigrade.accounts.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ReportedAccountController {
    @Autowired
    Environment env;
    @Autowired
    ReportedAccountService reportedAccountService;
    @Autowired
    AccountService accountService;

    @GetMapping("/get_reported_users")
    public String getAllReportedUsers(HttpSession session, Model model){
        Account a = (Account) session.getAttribute("account");
        if (a == null || a.getAccountType() != AccountType.ADMIN) {
            return "redirect:/";
        }

        List<ReportedAccount> all = reportedAccountService.getAllReportedAccounts();

        // For all reported accounts, set the account variable
        for(ReportedAccount ra : all){
            long accountId = ra.getReportedId();
            a = accountService.getAccountById(accountId);
            ra.setAccount(a);
        }

        model.addAttribute("reportedUsers", all);
        return "review_reported_users";
    }

    @PostMapping("/report_user")
    public String reportUser(long reportedId, String message, Model model){
        ReportedAccount ra = new ReportedAccount();
        ra.setReportedId(reportedId);
        ra.setAccount(accountService.getAccountById(reportedId));
        ra.setMessage(message);
        reportedAccountService.saveReportedAccount(ra);

        String successMsg = env.getProperty("report_user_success");
        model.addAttribute("notificationTitle", "Success!");
        model.addAttribute("notificationDetails", successMsg);

        return "notification_alert";
    }

    @PostMapping("remove_reported_user")
    public String removeReportedUser(long id, Model model){
        ReportedAccount ra = reportedAccountService.getReportedAccountById(id);
        reportedAccountService.deleteReportedAccount(ra);

        String successMsg = env.getProperty("report_user_allow");

        model.addAttribute("notificationTitle", "Success!");
        model.addAttribute("notificationDetails", successMsg);

        return "notification_alert";
    }
}
