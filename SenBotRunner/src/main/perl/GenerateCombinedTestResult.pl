use strict;
use Getopt::Std;
use File::Find;

our %options;

#
# Prints help message
#
sub helpMessage
{
    print "\nScanns through all subfolders and generates a combined report in the giben base directory\n";
    print "Command line parameters\n";
    print "\t -b base path from which to run from\n";
    exit;
}

#
# Runs the whole thing
#
sub main
{
    getopt("b:d:h",\%options);
    helpMessage() if (not defined $options{b}) or (defined $options{h});   
    
    unless (-e $options{b})
    {
         print "\n Base path wrong. Path does not exist. Terminated.\n"; 
         exit;
    }

    our @dirList; 
    find ( sub { next unless -d; push(@dirList, $File::Find::name); },  $options{b} );
    
    open (OUT, ">$options{b}\\combined_index.html") or die "Can not write to $options{d}\\index.html";
    print OUT "<!DOCTYPE html>\n";
    print OUT "<html>\n";
    print OUT "<head>\n";
    print OUT "<meta charset='utf-8'>\n";
    print OUT "<title>Combined test results</title>\n";

    my $replacePrefix = "$options{b}";
    $replacePrefix =~ tr/\\/\//;
    foreach my $ii (@dirList) 
    {
        $ii =~ tr/\\/\//;

        print "$ii\n";
        print "$replacePrefix\n\n";
        
        $ii =~ s/$replacePrefix\///;
        $ii =~ s/$replacePrefix//;
        if ($ii eq "") 
        {
            print OUT "<link href='style.css' rel='stylesheet'>\n";
            print OUT "<script src='jquery-1.8.2.min.js'></script>\n";
            print OUT "<script src='formatter.js'></script>\n";
            print OUT "<script src='report.js'></script>\n";
        } else {
            print OUT "<link href='$ii\\style.css' rel='stylesheet'>\n";
            print OUT "<script src='$ii\\jquery-1.8.2.min.js'></script>\n";
            print OUT "<script src='$ii\\formatter.js'></script>\n";
            print OUT "<script src='$ii\\report.js'></script>\n";
        }
    }
    
    
    print OUT "</head>\n";
    print OUT "<body>\n";
    print OUT "<div class='cucumber-report'></div>\n";
    print OUT "</body>\n";
    print OUT "</html>\n";
    close OUT;
    
}

main();


