from fpdf import FPDF
from datetime import datetime
import requests
import sys
import os


def next_table(pdf, data, w, h):
	pdf.ln(0.5)
	for row in data:
		for item in row:
			pdf.cell(w, 2 * h, f'{item}', border = 1, align = 'C')
		pdf.ln(2 * h)
	pdf.ln(0.5)


course_endpoint = 'http://localhost:8080/api/v1/applications/reports'
token = sys.argv[1]
headers = {'Authorization': f'Bearer {token}'}

pdf = FPDF(orientation = 'P', unit = 'in', format = 'A4')
pdf.add_page()
pdf.set_font('Times', '', 12.0)
epw = pdf.w - 2 * pdf.l_margin

response = requests.get(course_endpoint, headers = headers)
if (response.status_code == requests.codes.ok):
	json_data_list = response.json()
	for json_data in json_data_list:
		course = json_data['course']
		name = course['name']
		left_seats = course['leftSeats']
		total_seats = course['seats']
		table_head = f'{name} ({left_seats}/{total_seats})'

		table_data = [["Email", "Exams", "Status"]]
		for application in json_data['applications']:
			table_data.append([
				application['user']['login'], 
				application['examScore'], 
				application['status']
				])

		pdf.set_font('Times', 'B', 14.0) 
		pdf.cell(epw, 0.0, table_head, align = 'C')
		pdf.set_font('Times', '', 12.0)
		next_table(pdf, table_data, epw / 3, pdf.font_size)

full_path = os.path.realpath(__file__)
pdf.output(os.path.dirname(full_path) + "/reports/report-"+ str(datetime.date(datetime.now())) + ".pdf")

