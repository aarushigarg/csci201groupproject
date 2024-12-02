from flask import Flask, request
from openai import OpenAI

app = Flask(__name__)

@app.route('/process', methods=['POST'])
def process():
   data = request.json
   print(str(data))
   client = OpenAI()
   completion = client.chat.completions.create(
      model="gpt-4o",
      messages=[
          {"role": "system", "content": "You are a helpful assistant for an exercise tracking project. Your job is to provide science-backed suggestions for a person's exercise schedule given their exercises from the previous seven days, their goal, their body metrics, and the current date. You will be prompted with a JSON-formatted list containing their exercises from the previous seven days. Each exercise contains several attributes, but you only need to worry about \"date\", \"name\", \"repetitions\" (when applicable, based on the name), \"sets\" (when applicable), and \"durationMins\" (when applicable). The attributes within each exercise that can be ignored are \"id\", \"userId\", and \"isAISuggestion\". After considering the exercises they've done, consider the remaining attributes:  \"age\", which contains their age in years; \"gender\", which contains their biological sex; \"height\", which contains their height in inches; \"weight\", which contains their weight in pounds; \"goal\", which contains a short description of what they want to achieve by working out; and \"currDate\", the current date (to account for rest depending on their previous workout dates). Your response should come in the form of a single JSON-formatted string containing a single key: \"workout_rec\". The value of workout_rec should be a list of Exercises containing only the five attributes enumerated previously (YYYY-MM-DD date, string name,  int repetitions, int sets, int durationMins), with at least one Exercise for each of the following seven days. Feel free to add more than one per day if it makes sense according to the provided personal information. To represent a rest day, use name \"Rest\" and assign repetitions/sets/durationMin a value of 0. Respond only with the JSON. Make sure to format all dates in YYYY-MM-DD format."},
          {
              "role": "user",
              "content": str(data)
          }
      ]
   )
   print(completion.choices[0].message.content.strip('`').strip("json"))
   # d = eval('{"data":[{"key":"sample key","value":1}]}')
   # print(type(d))
   return eval(completion.choices[0].message.content.strip('`').strip("json"))

if __name__ == '__main__':
   app.run(port=5000)