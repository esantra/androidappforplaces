import sys
import os.path
import csv
import math 
import types
import random
#python defaultdict is a way to generate a keyed list
from collections import defaultdict, Iterable
import itertools

#difference between sets and lists in python
#Sets can't contain duplicates
#Sets are unordered
#In order to find an element in a set, a hash lookup is used (which is why sets are unordered). 
#This makes __contains__ (in operator) a lot more efficient for sets than lists.
#Sets can only contain hashable items (see #3). If you try: set(([1],[2])) you'll get a TypeError.
# --- in python arrays are called lists


class Apriori:
    returnPlace1 = "returnPlace1 " + " returnPlace2"
    def __init__(this, data, minSupport, minConfidence):
        this.dataset = data
        this.placeList = defaultdict(list)
        this.freqList = defaultdict(int)
        this.placeset = set()
        this.highSupportList = list()
        this.numplaces = 0
        this.prepData()            

        this.FirstPass = defaultdict(list)
        this.minSupport = minSupport
        this.minConfidence = minConfidence

    def generateAssociations(this):
		#associations occur when one item is purchased with another 
		#or one location is queried with another
        candidate = {}
        count = {}

        this.FirstPass[1] = this.onePass(this.freqList, 1)
        k=2
        while len(this.FirstPass[k-1]) != 0:
            candidate[k] = this.candidateGen(this.FirstPass[k-1], k)
            for t in this.placeList.iteritems():
                for c in candidate[k]:
                    if set(c).issubset(t[1]):
                        this.freqList[c] += 1
			#prune the candidates on the first pass
            this.FirstPass[k] = this.prune(candidate[k], k)
            if k > 2:
                this.removeSkyline(k, k-1)
            k += 1

        return this.FirstPass

    def removeSkyline(this, k, kPrev):
		#removes bad 
		#for each place in the first pass of this object
        for place in this.FirstPass[k]:
			#generate subsets from the place
            subsets = this.genSubsets(place)
			#for each subset in subsets, remove a subset if it is part of the previous subset
            for subset in subsets:
                if subset in (this.FirstPass[kPrev]):
                    this.FirstPass[kPrev].remove(subset)
                    

        subsets = this.genSubsets

    def prune(this, places, k):
        f = []
		#for each place in places
        for place in places:
			#count is equal to return from freqList (place#)
            count = this.freqList[place]
			#support is equal to float(count)/this.numplaces
            support = this.support(count)
			#if count/number of places is greater than 95%, add the place to the highSupportList, otherwise
			#just append to the regular places list
            if support >= .95:
                this.highSupportList.append(place)
            elif support >= this.minSupport:
                f.append(place)
		#return the array of regular places and not highSupportList
        return f

    def candidateGen(this, places, k):
		#declare candidate array for candidate generation
        candidate = []

        if k == 2:
			#if k is 2
			#a tuple is like a list enclosed in parenthesis - a list is enclosed in brackets
			#this line assigns a candidate vis sorting x and y
            candidate = [tuple(sorted([x, y])) for x in places for y in places if len((x, y)) == k and x != y]
        else:
            candidate = [tuple(set(x).union(y)) for x in places for y in places if len(set(x).union(y)) == k and x != y]
        
        for c in candidate:
			#for each candidate, generate a subset
            subsets = this.genSubsets(c)
			#if x is not in places but is in subset, then remove
            if any([ x not in places for x in subsets ]):
                candidate.remove(c)
		#the candidate set is returned as a result of the candidateGeneration method
        return set(candidate)

    def genSubsets(this, place):
        subsets = []
        for i in range(1,len(place)):
			#generate a subset
            subsets.extend(itertools.combinations(place, i))
        return subsets

    def support(this, count):
		#return count/number of places in this this
        return float(count)/this.numplaces

    def onePass(this, places, k):
        op = []
		#iterate counter count number of times in places
        for place, count in places.iteritems():
            support = this.support(count)
			#if support is equal to one, add to the high support list
            if support == 1:
                this.highSupportList.append(place)
			#else if support is less than or equal to minimum support, add to the regular place list
            elif support >= this.minSupport:
                op.append(place)

        return op
		
		
    def prepData(this):
        key = 0
		# basket is another name for dictionary and looks like this: basket1 = {"ham":2,"eggs":3}
        for basket in this.dataset:
			#increase the number of places for each basket of information {"Wiley's Bar and Grill", "bar"} would be one place
            this.numplaces += 1
            key = basket[0]
			#for each element in the basket ie ham, 2, eggs, 3
            for i, place in enumerate(basket):
                if i != 0:
                    this.placeList[key].append(place.strip())
                    this.placeset.add(place.strip())
                    this.freqList[(place.strip())] += 1
	
def one():
	#set places equal to a list value
    places = defaultdict(list)
    minSupport = minConfidence = 0
    place1 = ""
    place2 = ""

    dataset = csv.reader(open('data/1000/1000-out1.csv', "r"))
    placesData = csv.reader(open('places.csv', "r"))
    minSupport  = float(.03)
    minConfidence = float(.7)

	#assign each place to an array value in python
	#this is the candidate itemset of size placesData
    for place in placesData:
        places[place[0]] = place[1:]

    a = Apriori(dataset, minSupport, minConfidence)

    #frequent itemset of size placesData
	# this line assigns a variable the outcome of the generateAssociations method for the a Apriori object
    frequentplacesets = a.generateAssociations()

    counter = 0
    counter2 = 0
    var = []
	
	#for (k=1; Lk !=0; k++) do begin
	#Ck + 1 = candidates generated from Lk;
    for k, place in frequentplacesets.iteritems():
        for i in place:
            if k >= 2:
				#increment the count of all candidates in Ck+1 that are contained in t
                counter += 1
	    placeStr = ''
		#for each record append to item string
		#placecount = 0
	    for k, n in enumerate(i):
		#placecount += 1
		placeStr += places[n][0] + " "
	    	if len(place) != 0 and k != len(place)-1:
	    		placeStr += ",\t"		
	    	global returnPlace1
	    	returnPlace1 = placeStr
			var.append(returnPlace1)
	    	#print "returnPlace = " + returnPlace1	
	
		
	printvar =(random.choice(var))
	print printvar
    return printvar

def main():
	#set places equal to a list value
    places = defaultdict(list)
    minSupport = minConfidence = 0
    place1 = ""
    place2 = ""

    dataset = csv.reader(open('data/1000/1000-out1.csv', "r"))
    placesData = csv.reader(open('places.csv', "r"))
    minSupport  = float(.03)
    minConfidence = float(.7)

	#assign each place to an array value in python
	#this is the candidate itemset of size placesData
    for place in placesData:
        places[place[0]] = place[1:]

    a = Apriori(dataset, minSupport, minConfidence)

    #frequent itemset of size placesData
	# this line assigns a variable the outcome of the generateAssociations method for the a Apriori object
    frequentplacesets = a.generateAssociations()

    counter = 0
	
	#for (k=1; Lk !=0; k++) do begin
	#Ck + 1 = candidates generated from Lk;
    for k, place in frequentplacesets.iteritems():
        for i in place:
            if k >= 2:
				#increment the count of all candidates in Ck+1 that are contained in t
                counter += 1
	    placeStr = ''
		#for each record append to item string
	    for k, n in enumerate(i):
		placeStr += places[n][0] + " "
	    	if len(place) != 0 and k != len(place)-1:
	    		placeStr += ",\t"		
	    	global returnPlace1
	    	returnPlace1 = placeStr
	    	print "returnPlace " + i + " = " + returnPlace1
    return returnPlace1
	
def readable(item, goods):
    itemStr = ''
    for k, i in enumerate(item):
        itemStr += goods[i][0] + " " + goods[i][1] +" (" + i + ")"
        if len(item) != 0 and k != len(item)-1:
            itemStr += ",\t"

    return itemStr.replace("'", "")

if __name__ == '__main__':
    main()
