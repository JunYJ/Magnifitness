//
//  RWDetailViewController.h
//  Magnifitness
//
//  Created by Rowan on 7/12/14.
//  Copyright (c) 2014 Rowan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RWDetailViewController : UIViewController

@property (strong, nonatomic) id detailItem;

@property (weak, nonatomic) IBOutlet UILabel *detailDescriptionLabel;
@end
