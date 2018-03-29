def main():
    total = 0.0
    length = 0.0
    average = 0.0

try:
        TS = raw_input('Enter the TS file name and path, if you do not know, type ts.txt: ')
        TJ = raw_input('Enter the TJ file name and path, if you do not know, type tj.txt: ')

        totalTS = 0.0
        lengthTS = 0.0
        averageTS = 0.0

        totalTJ = 0.0
        lengthTJ = 0.0
        averageTJ = 0.0
        
        #Open files
        inTS = open(TS, 'r')
        inTJ = open(TJ, 'r')

        # Read values from TS file and compute average
        for line in inTS:
            print line.rstrip("\n")
            amountTS = float(line.rstrip("\n"))
            totalTS += amountTS
            lengthTS = lengthTS + 1

        averageTS = (totalTS / lengthTS)/1000000

        
        # Read values from TJ file and compute average
        for line in inTJ:
            print line.rstrip("\n")
            amountTJ = float(line.rstrip("\n"))
            totalTJ += amountTJ
            lengthTJ = lengthTJ + 1

        averageTJ = (totalTJ / lengthTJ)/1000000

        
        # Close the file
        inTS.close()
        inTJ.close()
        
        # Print the amount of numbers in file and average
        print 'There were', lengthTS, 'values in the TS file. The average is: '
        print format(averageTS, ',.2f'), 'ms'

        print 'There were', lengthTJ, 'values in the TJ file. The average is: '
        print format(averageTJ, ',.2f'), 'ms'

except IOError:
        print 'An error occurred trying to read the file.'

except ValueError:
        print 'Non-numeric data found in the file'
except:
        print('An error has occurred')

main()