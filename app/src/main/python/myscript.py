# def main():
#     return "It is working..."

import speech_recognition as sr
from gtts import gTTS
import os
from pygame import mixer
from happytransformer import  HappyTextToText
import warnings
warnings.filterwarnings('ignore') # setting ignore as a parameter
happy_tt = HappyTextToText("T5", "vennify/t5-base-grammar-correction")
#Grammar correction part
from happytransformer import TTSettings
beam_settings =  TTSettings(num_beams=5, min_length=1, max_length=100)
#function for getting voice as input

def listenp():
    # Initialize recognizer class (for recognizing the speech)
    r = sr.Recognizer()

    # Reading Microphone as source
    # listening the speech and store in audio_text variable
    with sr.Microphone() as source:
        print("Talk")
        audio_text = r.listen(source)
        print("Time over, thanks")

    # recoginize_() method will throw a request error if the API is unreachable
    try:
        # using google speech recognition
        return r.recognize_google(audio_text)
    except sr.RequestError as e:
        print("Could not request results from Google Speech Recognition service; {0}".format(e))
    except sr.UnknownValueError:
        print("Sorry, I did not get that")
        return None
    
    # Setup for voice output
import pyttsx3
engine =pyttsx3.init('sapi5')
voices =engine.getProperty('voices')
engine.setProperty('voice',voices[0].id)
engine.setProperty('rate',150)
print(voices[1].id)

#Setup for Paraphrasing of the input
import torch
from transformers import PegasusForConditionalGeneration, PegasusTokenizer

model_name = 'tuner007/pegasus_paraphrase'
torch_device = 'cuda' if torch.cuda.is_available() else 'cpu'
tokenizer = PegasusTokenizer.from_pretrained(model_name)
model = PegasusForConditionalGeneration.from_pretrained(model_name).to(torch_device)

def get_response(input_text,num_return_sequences):
  batch = tokenizer.prepare_seq2seq_batch([input_text],truncation=True,padding='longest',max_length=60, return_tensors="pt").to(torch_device)
  translated = model.generate(**batch,max_length=60,num_beams=10, num_return_sequences=num_return_sequences, temperature=1.5)
  tgt_text = tokenizer.batch_decode(translated, skip_special_tokens=True)
  return tgt_text

#Listening from the user
text = listenp()
#Confirming if the program has listend the user
engine.say(text)
engine.runAndWait()
# Responsible for grammar correction
input_text_1 = "grammar: "+text
output_text_1 = happy_tt.generate_text(input_text_1, args=beam_settings)
print(output_text_1.text)
engine.say(output_text_1.text)
engine.runAndWait()
get_response(output_text_1.text, 5)
get_response(text, 1)
engine.say("You could have said this in following ways :")
engine.runAndWait()
engine.say(get_response(output_text_1.text, 5))
engine.runAndWait()