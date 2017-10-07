//
//  mainViewController.swift
//  mobileProject2_ver3
//
//  Created by Jianqiang Zhang on 5/10/17.
//  Copyright © 2017 Jianqiang Zhang. All rights reserved.
//

import UIKit

class mainViewController: UIViewController {
    @IBOutlet var txtEmailAddress: UITextField!
    @IBOutlet var txtPasswd: UITextField!
    @IBAction func editEmailAddress(_ sender: Any) {
        txtEmailAddress.text = ""
    }
    @IBAction func editPasswd(_ sender: Any) {
        txtPasswd.text = ""
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}



