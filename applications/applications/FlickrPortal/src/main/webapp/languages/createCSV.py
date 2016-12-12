import json
import csv
import os
import glob
import re

extractName = re.compile(r'.*[/\\]+(.+)\.lang\.json')

languages = sorted([f for f in os.listdir('.') if os.path.isdir(f)])

data = {}
keys = []
for l in languages:
	data[l] = {}
	for filePath in glob.glob('%s/*.json' % l):
		with open(filePath, 'r', encoding='utf-8') as f:
			fileName = extractName.match(filePath).group(1)
			data[l][fileName] = {}
			fileData = json.load(f)
			for k in fileData:
				data[l][fileName][k] = fileData[k]
				key = (fileName, k)
				if key not in keys:
					keys.append(key)
keys = sorted(keys)

try:
	print(data)
except:
	print("Could not print the data")

with open('localization.csv', 'w', encoding='utf-8') as csvOut:
	csvWriter = csv.writer(csvOut, dialect='unix')
	csvWriter.writerow(['keys'] + languages)
	for key in keys:
		csvWriter.writerow([key[0] + '.' + key[1]] + [data[l][key[0]][key[1]] for l in languages])

print("CSV created successfully")
input("press [enter] to exit")