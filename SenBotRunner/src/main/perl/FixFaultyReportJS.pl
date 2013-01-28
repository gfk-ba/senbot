use strict;
use Getopt::Std;
use File::Find;

our %options;

#
# Prints help message
#
sub helpMessage
{
    print "\nFixes the flaw in report.js result files\n";
    print "Command line parameters\n";
    print "\t -b base path from which to run from\n";
    exit;
}

sub wanted
{
    if ($_ ne "report.js") 
    {
        return;
    } 

    open (IN, "<$File::Find::name") or die "Can not read from $File::Find::name";
        
    my $fileContent;
    while(<IN>) {
        $fileContent = $fileContent . $_;
    }
    close IN;

    $fileContent =~  s/\"\\u0027/'/g;
    $fileContent =~  s/\\u0027\"/'/g;
    $fileContent =~  s/\\u0027/'/g;
    
    open (OUT, ">$File::Find::name") or die "Can not write to $File::Find::name";
    print OUT $fileContent;    
    close OUT;
}

#
# Runs the whole thing
#
sub main
{

    getopt("b:h",\%options);
    helpMessage() if (not defined $options{b}) or (defined $options{h});   
    
    unless (-e $options{b})
    {
         print "\n Base path wrong. Path does not exist. Terminated.\n"; 
         exit;
    }

    find(\&wanted, $options{b});
}

main();


