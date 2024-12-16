import dotenv, os, pathlib
# Import smtplib for the actual sending function
import smtplib
self_fullpath = pathlib.Path(os.path.join(os.getcwd(), __file__))
dotvalues = dotenv.dotenv_values(self_fullpath.parent.parent.parent/".env")


# Import the email modules we'll need
from email.message import EmailMessage

# Open the plain text file whose name is in textfile for reading.
# Create a text/plain message
msg = EmailMessage()
msg.set_content(f"Email body")

# me == the sender's email address
# you == the recipient's email address
msg['Subject'] = 'Test'
msg['From'] = "lucioric2000@gmail.com"
msg['To'] = "lucio.montero@ibt.unam.mx"

# Send the message via our own SMTP server.
s = smtplib.SMTP_SSL('smtp.gmail.com')
s.login(dotvalues["EMAIL_USER"], dotvalues["EMAIL_PASSWORD"])
s.send_message(msg)
s.quit()

