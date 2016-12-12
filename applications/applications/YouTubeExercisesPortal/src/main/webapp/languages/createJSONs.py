import json
import csv
import os

data = {}
with open('localization.csv', 'r', encoding='utf-8') as csvIn:
	csvReader = csv.reader(csvIn, dialect='unix')
	languages = next(csvReader)[1:]
	for row in csvReader:
		key = row[0].split('.')
		fileName = key[0]
		k = key[1]
		for i in range(len(languages)):
			l = languages[i]
			if not data.get(l):
				data[l] = {}
			if not data[l].get(fileName):
				data[l][fileName] = {}
			data[l][fileName][k] = row[i + 1]

try:
	print(data)
except:
	print("Could not print the data")

for l in languages:
	if not os.path.exists(l):
		os.makedirs(l)
	for fileName in data[l]:
		with open(l + '/' + fileName + '.lang.json', 'w', encoding='utf-8') as f:
			json.dump(data[l][fileName], f, ensure_ascii=False, sort_keys=True, indent=4)

print("JSONs created successfully")
input("press [enter] to exit")