/*
InfiniteFluids Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package josephcsible.infinitefluids;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class InfiniteFluidsClassTransformer implements IClassTransformer {
	private static String updateTickName, updateTickDesc, fluidIsInfiniteDesc, maybeCreateSourceBlockDesc;

	public static void setObfuscated(boolean isObfuscated) {
		if(isObfuscated) {
			updateTickName = "a";
			updateTickDesc = "(Lahb;IIILjava/util/Random;)V";
			fluidIsInfiniteDesc = "(Laji;Lahb;)Z";
			maybeCreateSourceBlockDesc = "(Lnet/minecraftforge/fluids/BlockFluidClassic;Lahb;III)V";
		} else {
			updateTickName = "updateTick";
			updateTickDesc = "(Lnet/minecraft/world/World;IIILjava/util/Random;)V";
			fluidIsInfiniteDesc = "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;)Z";
			maybeCreateSourceBlockDesc = "(Lnet/minecraftforge/fluids/BlockFluidClassic;Lnet/minecraft/world/World;III)V";
		}
	}

	private static void transformVanillaUpdateTick(MethodNode mn) {
		/*
		We're trying to change this:
		if (this.field_149815_a >= 2 && this.blockMaterial == Material.water)
		to this:
		if (this.field_149815_a >= 2 && InfiniteFluidsHooks.fluidIsInfinite(this, worldIn))

		Here's the relevant piece of the bytecode:
		L18
		LINENUMBER 72 L18
		FRAME CHOP 1
		ALOAD 0
		GETFIELD net/minecraft/block/BlockDynamicLiquid.field_149815_a : I
		ICONST_2 *** searched for
		IF_ICMPLT L23 *** searched for
		ALOAD 0 *** target node
		*** new stuff goes here
		GETFIELD net/minecraft/block/BlockDynamicLiquid.blockMaterial : Lnet/minecraft/block/material/Material; *** removed
		GETSTATIC net/minecraft/block/material/Material.water : Lnet/minecraft/block/material/Material; *** removed
		IF_ACMPNE L23 *** removed
		*/
		AbstractInsnNode targetNode = null;
		for (AbstractInsnNode instruction : mn.instructions.toArray())
		{
			if (instruction.getOpcode() == ICONST_2 && instruction.getNext().getOpcode() == IF_ICMPLT)
			{
				targetNode = instruction.getNext().getNext(); // this is ALOAD 0
				break;
			}
		}
		if (targetNode == null)
		{
			System.err.println("Failed to find the part of BlockDynamicLiquid.updateTick we need to patch!");
			return;
		}
		System.out.println("Patching BlockDynamicLiquid.updateTick");
		mn.instructions.remove(targetNode.getNext()); // remove GETFIELD
		mn.instructions.remove(targetNode.getNext()); // remove GETSTATIC
		JumpInsnNode n = (JumpInsnNode)targetNode.getNext();
		LabelNode ln = n.label;
		mn.instructions.remove(n); // remove IF_ACMPNE
		InsnList toInsert = new InsnList();
		toInsert.add(new VarInsnNode(ALOAD, 1));
		toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(InfiniteFluidsHooks.class), "fluidIsInfinite", fluidIsInfiniteDesc, false));
		toInsert.add(new JumpInsnNode(IFEQ, ln));
		mn.instructions.insert(targetNode, toInsert);
	}

	private static void transformForgeUpdateTick(MethodNode mn) {
		System.out.println("Patching BlockFluidClassic.updateTick");
		// We're adding this line to the beginning of the method:
		// InfiniteFluidsHooks.maybeCreateSourceBlock(this, world, x, y, z);
		Label oldBeginLabel = ((LabelNode)mn.instructions.getFirst()).getLabel();
		Label beginLabel = new Label();
		InsnList toInsert = new InsnList();
		toInsert.add(new LabelNode(beginLabel));
		toInsert.add(new VarInsnNode(ALOAD, 0));
		toInsert.add(new VarInsnNode(ALOAD, 1));
		toInsert.add(new VarInsnNode(ILOAD, 2));
		toInsert.add(new VarInsnNode(ILOAD, 3));
		toInsert.add(new VarInsnNode(ILOAD, 4));
		toInsert.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(InfiniteFluidsHooks.class), "maybeCreateSourceBlock", maybeCreateSourceBlockDesc, false));
		mn.instructions.insert(toInsert);
		// Make sure nothing looks like it's out of scope in our injected code
		Iterator<LocalVariableNode> iter = mn.localVariables.iterator();
		while(iter.hasNext()) {
			LocalVariableNode lvn = iter.next();
			if(lvn.start.getLabel() == oldBeginLabel) {
				lvn.start = new LabelNode(beginLabel);
			}
		}
	}

	private static ClassNode byteArrayToClassNode(byte[] basicClass) {
		ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cn, ClassReader.SKIP_FRAMES);
		return cn;
	}

	private static byte[] classNodeToByteArray(ClassNode cn) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cn.accept(cw);
		return cw.toByteArray();
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(transformedName.equals("net.minecraft.block.BlockDynamicLiquid")) {
			ClassNode cn = byteArrayToClassNode(basicClass);
			for(MethodNode mn : cn.methods) {
				if (mn.name.equals(updateTickName) && mn.desc.equals(updateTickDesc)) {
					transformVanillaUpdateTick(mn);
					return classNodeToByteArray(cn);
				}
			}
			System.err.println("Failed to find the BlockDynamicLiquid.updateTick method!");
		} else if(transformedName.equals("net.minecraftforge.fluids.BlockFluidClassic")) {
			ClassNode cn = byteArrayToClassNode(basicClass);
			for(MethodNode mn : cn.methods) {
				if (mn.name.equals(updateTickName) && mn.desc.equals(updateTickDesc)) {
					transformForgeUpdateTick(mn);
					return classNodeToByteArray(cn);
				}
			}
			System.err.println("Failed to find the BlockFluidClassic.updateTick method!");
		}
		return basicClass;
	}
}
