package cn.ce.platform_service.common.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ce.platform_service.util.PropertiesUtil;


public class MailUtil {

	
	private static Logger logger = LoggerFactory.getLogger(MailUtil.class);
	/**
	 * 发送邮件
	 * 
	 * @param mailInfo
	 *            邮件信息
	 * @param synchFlag
	 *            是否同步发送
	 * 
	 */
	public static void send(final MailInfo mailInfo, boolean synchFlag) {
		if (synchFlag) {// 同步发送
			doSend(mailInfo);
		} else {// 异步发送
			new Thread() {
				public void run() {
					doSend(mailInfo);
				};
			}.start();
		}

	}

	private static void doSend(MailInfo mailInfo) {
		String mailServer = PropertiesUtil.getInstance().getValue("mail.server");
		String user = PropertiesUtil.getInstance().getValue("mail.user");
		String password = PropertiesUtil.getInstance().getValue("mail.password");
		String protocol = PropertiesUtil.getInstance().getValue("mail.protocol");
		
		logger.info("mailServer:"+mailServer);
		logger.info("user:"+user);
		logger.info("password:"+password);
		logger.info("protocal:"+protocol);
//		String mailServer = "smtp.300.cn";
//		String user = "xxh@300.cn";
//		String password = "xxh@";
//		String protocol = "smtp";
		Transport ts = null;
		try {
			Properties prop = new Properties();
			prop.setProperty("mail.host", mailServer);
			prop.setProperty("mail.transport.protocol", protocol);
			prop.setProperty("mail.smtp.auth", "true");
			// 使用JavaMail发送邮件的5个步骤
			// 1、创建session
			Session session = Session.getInstance(prop);
			// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
			session.setDebug(false);
			// 2、通过session得到transport对象
			ts = session.getTransport();
			// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
			ts.connect(mailServer, user, password);
			// 4、创建邮件
			Message message = createSimpleMail(session, mailInfo, user);
			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		} finally {
			if (null != ts) {
				try {
					ts.close();
				} catch (MessagingException e) {
					throw new BusinessException(e.getMessage(), e);
				}
			}
		}

	}
	
	public static void main(String[] args) {
		MailInfo mailInfo = new MailInfo();
		mailInfo.setContent("nnnnn");
		mailInfo.setSubject("主题好好");

		mailInfo.setToOne("makangwei@300.cn");
		MailUtil.send(mailInfo, false);
	}

	private static MimeMessage createSimpleMail(Session session, MailInfo mailInfo, String user)
	                throws Exception { 
		// 设置发件人昵称
        String nick = MimeUtility.encodeText(PropertiesUtil.getInstance().getValue("mail.nick"));
        logger.info("mail.nick:"+nick);
		// 创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 指明邮件的发件人
		message.setFrom(new InternetAddress(nick + "<" + user + ">"));
		// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getToOne()));
		// 邮件的标题
		message.setSubject(mailInfo.getSubject(), "UTF8");
		// 邮件的文本内容
		message.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		return message;
	}

}
